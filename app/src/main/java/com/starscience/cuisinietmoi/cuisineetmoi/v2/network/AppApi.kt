package com.starscience.cuisinietmoi.cuisineetmoi.v2.network

import com.google.gson.internal.LinkedTreeMap
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Like
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.User
import io.reactivex.Observable
import retrofit2.http.*

interface AppApi {

    @GET("Recipes.json")
    fun getRecipes() : Observable<LinkedTreeMap<String, Recipe>>

    @GET("Recipes/{id}.json")
    fun getRecipe(@Path("id") recipeId: String) : Observable<Recipe>

    @PUT("Recipes/{id}.json")
    fun createRecipes(@Path("id") firebaseId: String?, @Body recipe: Recipe) : Observable<Recipe>

    @GET("Users/{id}.json")
    fun getUser(@Path("id") userId: String) : Observable<User>

    @PUT("Users/{id}.json")
    fun createUser(@Path("id") firebaseId: String, @Body user: User) : Observable<User>

    @PUT("Users/{id}/username.json")
    fun updateUsername(@Path("id") firebaseId: String, @Body username: String) : Observable<String>

    @PUT("Users/{id}/user_description.json")
    fun updateDescription(@Path("id") firebaseId: String, @Body description: String) : Observable<String>

    @GET("Likes/{id}.json")
    fun getLikes(@Path("id") userId: String) : Observable<LinkedTreeMap<String, String>>

    @DELETE("Likes/{userId}/{recipeId}.json")
    fun dislike(@Path("userId") userId: String, @Path("recipeId") recipeId: String) : Observable<Any>

    @PUT("Likes/{userId}/{recipeId}.json")
    fun like(@Path("userId") userId: String?, @Path("recipeId") id: String?, @Body likeString: String?) : Observable<String>

    @PUT("Recipes/{id}/likesCount.json")
    fun updateLikes(@Path("id") recipeId: String?, @Body newLikeCount: Int?) : Observable<Any>

}