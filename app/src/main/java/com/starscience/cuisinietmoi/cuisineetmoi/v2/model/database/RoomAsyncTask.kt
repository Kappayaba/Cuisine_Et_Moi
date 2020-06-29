package com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database

import android.os.AsyncTask
import com.google.firebase.auth.FirebaseAuth
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.*

class UpdateRoomRecipeAsyncTask(private val recipeDao: RecipeDao, private val newApiList: List<Recipe>) : AsyncTask<List<Recipe>, Any, Boolean>(){

    override fun doInBackground(vararg params: List<Recipe>?): Boolean {
        recipeDao.saveRecipe(*newApiList.toTypedArray())
        return true
    }
}

class UpdateRoomLikeAsyncTask(private val likeDao: LikeDao, private val userId: String, private val recipeId: String?, private val isLiked: Boolean) : AsyncTask<List<Like>, Any, Boolean>(){

    override fun doInBackground(vararg params: List<Like>?): Boolean {
        return if(isLiked){
            val userLikeResponse = likeDao.getLikes(userId)
            userLikeResponse.recipeIds[recipeId] = "IS_LIKED"

            likeDao.saveLike(userLikeResponse)
            true
        } else{
            val userLikeResponse = likeDao.getLikes(userId)
            userLikeResponse.recipeIds.remove(recipeId)

            likeDao.saveLike(userLikeResponse)
            false
        }
    }
}

class UpdateRoomUserInfoAsyncTask(private val userDao: UserDao, private val currentUserId: String, private val newUserName: String, private val newUserDescription: String) : AsyncTask<String, Any, Boolean>(){
    override fun doInBackground(vararg params: String?): Boolean {
        val user = userDao.getUser(currentUserId)[0]

        user.user_description = newUserDescription
        user.username = newUserName

        userDao.saveUser(user)
        return true
    }
}

class GetRoomUserLikesAsyncTask(private val likeDao: LikeDao, private val user_id: String): AsyncTask<String, Any, List<String>>(){
    override fun doInBackground(vararg params: String?): List<String> {
        val likesResponse = likeDao.getLikes(user_id)
        val likes = likesResponse.recipeIds.values.toList()
        return likes
    }
}