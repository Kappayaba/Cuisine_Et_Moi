package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.RecipeDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.UpdateRoomRecipeAsyncTask
import com.starscience.cuisinietmoi.cuisineetmoi.v2.network.AppApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

//Todo: Check if all elements aren't null and optimise it
class AddRecipeViewModel(private val recipeDao: RecipeDao) : BaseViewModel() {
    @Inject
    lateinit var appApi: AppApi

    lateinit var subscription: Disposable

    private val storageRef = FirebaseStorage.getInstance().getReference("uploads")
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val loadingImage: MutableLiveData<Int> = MutableLiveData()

    val recipeTitle: MutableLiveData<String> = MutableLiveData()
    val recipeTime: MutableLiveData<String> = MutableLiveData()
    val recipeCook: MutableLiveData<String> = MutableLiveData()
    val recipePerson: MutableLiveData<String> = MutableLiveData()
    val recipeImage: MutableLiveData<String> = MutableLiveData()

    val recipeIngredients: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val recipeInstruction: MutableLiveData<ArrayList<String>> = MutableLiveData()

    val publishedRecipe: MutableLiveData<Boolean> = MutableLiveData()

    init {
        recipeTitle.value = "New Recipe"
        recipeTime.value = "1h 15m"
        recipeCook.value = "55m"
        recipePerson.value = "4"
        recipeImage.value = "default"

        loadingImage.value = View.GONE

        recipeIngredients.value = arrayListOf("Some ingredients here")
        recipeInstruction.value = arrayListOf("Some instructions here")

        publishedRecipe.value = false
    }

    fun publishRecipe(v: View){
        loadingImage.value = View.VISIBLE
        val dateFormat =  SimpleDateFormat("dd/MM/yyyy")
        val date = dateFormat.format(Calendar.getInstance().time)
        val user = FirebaseAuth.getInstance().currentUser
        val key = FirebaseDatabase.getInstance().getReference("Recipes").push().key
        if(recipeIngredients.value?.isEmpty() == true){
            recipeIngredients.value = arrayListOf("Some ingredients here")
        }
        if(recipeInstruction.value?.isEmpty() == true){
            recipeInstruction.value = arrayListOf("Some instructions here")
        }
        val recipe = Recipe(key.toString(), recipeTitle.value, recipeImage.value, user?.uid ?: "", date, 0, 0, recipePerson.value ?: "4", recipeTime.value, recipeCook.value, recipeIngredients.value, recipeInstruction.value)
        UpdateRoomRecipeAsyncTask(recipeDao, listOf(recipe)).execute()
        subscription = appApi.createRecipes(recipe.id, recipe)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { publishedRecipe.value = true },
                { e -> onError(e)}
            )
    }

    fun uploadImage(uri: Uri?, extension: String){
        loadingImage.value = View.VISIBLE
        if(uri != null){
            val filePath: String = System.currentTimeMillis().toString() + "." + extension
            val fileRef= storageRef.child(filePath)
            Log.w("HAMZA", "$uri")
            fileRef.putFile(uri)
                .addOnSuccessListener { fileRef.downloadUrl
                    .addOnSuccessListener { result -> onDownloadSuccess(result) }
                    .addOnFailureListener { e -> onError(e) }
                }
                .addOnFailureListener{ e -> onError(e) }
        }
        else{
            val e = Exception("No image selected")
            onError(e)
        }
    }

    private fun onDownloadSuccess(result: Uri) {
        setImage(result.toString())
        loadingImage.value = View.GONE
    }

    private fun onError(e: Throwable) {
        loadingImage.value = View.GONE
        errorMessage.value = e.message
    }

    fun setTitle(title: String){
        recipeTitle.value = title
    }

    fun setTime(time: String){
        recipeTime.value = time
    }

    fun setCook(cook: String){
        recipeCook.value = cook
    }

    fun setPerson(person: String){
        recipePerson.value = person
    }

    fun setImage(imageUrl: String){
        recipeImage.value = imageUrl
    }

    fun addIngredients(ingredient: String){
        recipeIngredients.value?.add(ingredient)
    }

    fun deleteIngredients(i: Int){
        recipeIngredients.value?.removeAt(i)
        Log.w("Inchallah ", "${recipeIngredients.value}")
    }
     fun addInstruction(instruction: String){
        recipeInstruction.value?.add(instruction)
    }

    fun deleteInstruction(i: Int){
        recipeInstruction.value?.removeAt(i)
    }

}