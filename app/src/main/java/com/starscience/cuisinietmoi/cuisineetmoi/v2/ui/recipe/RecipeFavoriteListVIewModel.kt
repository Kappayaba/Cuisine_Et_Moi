package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.internal.LinkedTreeMap
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.main_fragment.FavoriteFragment
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.LikeDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.RecipeDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.network.AppApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipeFavoriteListVIewModel(private val recipeDao: RecipeDao, private val likeDao: LikeDao) : BaseViewModel() {
    @Inject
    lateinit var appApi: AppApi
    lateinit var subscription: Disposable

    val favRecipeList: MutableLiveData<ArrayList<Recipe>> = MutableLiveData()
    val clickedRecipe: MutableLiveData<Recipe> = MutableLiveData()
    val favoriteListAdapter = RecipeListAdapter() { recipe -> clickedRecipe.value = recipe }
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    init {
        loadFavorites(userId)
    }

    private fun loadFavorites(userId: String?) {
        subscription = Observable.fromCallable{ likeDao.getLikes(userId ?: "") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> doOnRetrieveFavSuccess(result.recipeIds.keys.toList()) },
                { e -> doOnRetrieveFavError(e) }
            )
    }

    private fun doOnRetrieveFavSuccess(userLikes: List<String>) {
        subscription = Observable.fromCallable{ recipeDao.all }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onRetrieveRecipe(result, userLikes) },
                { e -> doOnRetrieveFavError(e) }
            )
    }

    private fun onRetrieveRecipe(result: List<Recipe>, userLikes: List<String>) {
        val placeHolder = ArrayList(result)
        for (recipe in result){
            if(recipe.id !in userLikes){
                placeHolder.remove(recipe)
            }
        }
        favoriteListAdapter.updateRecipeList(placeHolder, userLikes)
    }


    private fun doOnRetrieveFavError(e: Throwable?) {
        Log.w("ERROR FAVORITE HAMZA", "$e")
    }

}