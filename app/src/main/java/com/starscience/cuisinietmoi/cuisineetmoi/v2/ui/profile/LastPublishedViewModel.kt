package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.LikeDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.RecipeDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.UserDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.response.LikeResponse
import com.starscience.cuisinietmoi.cuisineetmoi.v2.network.AppApi
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe.RecipeListAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class LastPublishedViewModel(private val recipeDao: RecipeDao, private val likeDao: LikeDao) : BaseViewModel(), ValueEventListener {
    @Inject
    lateinit var appApi: AppApi
    lateinit var subscription: Disposable
    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val userRecipeAdapter = RecipeListAdapter() { recipe -> Log.w("test", "$recipe") }

    init {
        loadingVisibility.value = View.VISIBLE
        loadUserLikes()
    }

    private fun loadUserLikes() {
        subscription = Observable.fromCallable{ likeDao.getLikes(userId) }
            .concatMap { dbList ->
                Observable.just(dbList.recipeIds.keys.toList())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> loadRecipes(result) },
                { e -> onError(e) }
            )
    }

    private fun loadRecipes(likes: List<String>) {
        subscription = Observable.fromCallable{ recipeDao.all }
            .concatMap { dbList ->
                Observable.just(dbList)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> doOnResult(result, likes) },
                {e -> onError(e)}
            )
    }

    private fun doOnResult(result: List<Recipe>, likes: List<String>)   {
        loadingVisibility.value = View.GONE
        val placeholder = mutableListOf<Recipe>()
        for (e in result){
            if(e.userId == FirebaseAuth.getInstance().currentUser?.uid){
                placeholder.add(e)
            }
        }
        userRecipeAdapter.updateRecipeList(placeholder.reversed(), likes)
    }

    private fun onError(e: Throwable?) {
        Log.w("LAST PUBLISHED ERROR", "$e")
        loadingVisibility.value = View.GONE
    }

    override fun onCancelled(p0: DatabaseError) {
        Log.w("ON CANCELED ERROR LPVM", p0.message)
        loadingVisibility.value = View.GONE
    }

    override fun onDataChange(p0: DataSnapshot) {
        val hashMap = p0.value as HashMap<String, Recipe>
        val values = hashMap.values
        for (e in values){
            Log.w("ON DATA CHANGED LPVM", "$e")
        }
    }


}