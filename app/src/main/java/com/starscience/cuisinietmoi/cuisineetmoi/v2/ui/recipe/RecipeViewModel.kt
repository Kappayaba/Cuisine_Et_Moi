package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.User
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.AppDatabase
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.UpdateRoomLikeAsyncTask
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.UpdateRoomRecipeAsyncTask
import com.starscience.cuisinietmoi.cuisineetmoi.v2.network.AppApi
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.STRING_LIKED
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

// Todo : Cache the image to accelerate the loading process
class RecipeViewModel(private val db: AppDatabase) : BaseViewModel() {
    @Inject
    lateinit var appApi: AppApi
    lateinit var subscription: Disposable
    private lateinit var recipe: Recipe

    private val userDao = db.userDao()
    private val likeDao = db.likeDao()
    private val recipeDao = db.recipeDao()

    private var recipeId: String? = null
    private val recipeName = MutableLiveData<String>()
    private val recipeImage = MutableLiveData<String>()
    private val recipePostDate = MutableLiveData<String>()
    private val recipeLikesCount = MutableLiveData<String>()
    private val recipeReplyCount = MutableLiveData<String>()
    private val userProfileImage = MutableLiveData<String>()
    private val username = MutableLiveData<String>()

    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

    private val isLiked: MutableLiveData<Boolean> = MutableLiveData()

    fun bind(recipe: Recipe, userLiked: Boolean){
        loadUser(recipe.userId)
        this.recipe = recipe
        recipeId = recipe.id
        recipeName.value = recipe.name
        recipeImage.value = recipe.imageUrl
        recipePostDate.value = recipe.postDate
        recipeLikesCount.value = recipe.likesCount.toString()
        recipeReplyCount.value = recipe.replyCount.toString()

        isLiked.value = userLiked
    }

    // getter
    fun getRecipeName() : MutableLiveData<String>{
        return recipeName
    }

    fun getRecipeImage() : MutableLiveData<String>{
        return recipeImage
    }

    fun getRecipePostDate() : MutableLiveData<String>{
        return recipePostDate
    }

    fun getRecipeLikesCount() : MutableLiveData<String>{
        return recipeLikesCount
    }

    fun getRecipeReplyCount() : MutableLiveData<String>{
        return recipeReplyCount
    }

    fun getRecipeUsername() : MutableLiveData<String>{
        return username
    }

    fun getRecipeUserProfileImage() : MutableLiveData<String>{
        return userProfileImage
    }

    fun getIsLiked() : MutableLiveData<Boolean>{
        return isLiked
    }

    //setter
    fun setIsLiked(v: View){
        isLiked.value = isLiked.value != true
        if (isLiked.value == true){
            recipeLikesCount.value = recipeLikesCount.value?.toInt()?.plus(1).toString()
            likeRecipe(currentUserId, recipeId)

        }
        else{
            recipeLikesCount.value = recipeLikesCount.value?.toInt()?.minus(1).toString()
            Log.w("TEST", recipeLikesCount.value ?: "")
            dislikeRecipe(currentUserId, recipeId)
        }
    }

    private fun likeRecipe(userId: String?, recipeId: String?) {
        UpdateRoomLikeAsyncTask(likeDao, userId!!, recipeId, true).execute()
        subscription = appApi.like(userId, recipeId, STRING_LIKED)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { updateLikes(recipeLikesCount.value?.toInt()) },
                {  }
            )

    }

    private fun dislikeRecipe(currentUserId: String?, recipeId: String?) {
        UpdateRoomLikeAsyncTask(likeDao, currentUserId!!, recipeId, false).execute()
        subscription = appApi.dislike(currentUserId.toString(), recipeId.toString())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { updateLikes(recipeLikesCount.value?.toInt()) },
                { e -> onLikeError(e, -1) }
            )
    }

    private fun updateLikes(newLikesCountValue: Int?) {
        subscription = appApi.updateLikes(recipeId, newLikesCountValue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { subscription = appApi.getRecipes().subscribe{ recipes -> UpdateRoomRecipeAsyncTask(recipeDao, recipes.values.toList()).execute() } },
                { e -> onLikeError(e, 0) }
            )
    }

    private fun onLikeError(e: Throwable?, incrementation: Int?) {
        Log.w("~~LIKE ERROR~~", "$e")
        recipeLikesCount.value = recipeLikesCount.value?.toInt()?.plus(incrementation ?: 0).toString()
        isLiked.value = false
    }


    // Retrieve User from database
    private fun loadUser(userId: String){
        subscription = Observable.fromCallable{ userDao.getUser(userId) }
            .concatMap { dbUser ->
                if(dbUser.isEmpty()){
                    appApi.getUser(userId).concatMap { apiUser -> userDao.saveUser(apiUser)
                    Observable.just(apiUser)}
                }
                else {
                    Observable.just(dbUser[0])
                }
            }
            .subscribeOn(Schedulers.io())
            .subscribe(
                {result -> onRetrieveUserSuccess(result)},
                {e -> onError(e)}
            )
    }

    private fun onRetrieveUserSuccess(user: User) {
        userProfileImage.postValue(user.profil_image)
        username.postValue(user.username)
    }

    private fun onError(e : Throwable) {
        Log.w("RECIPE VM ERROR", "$e")
    }

}