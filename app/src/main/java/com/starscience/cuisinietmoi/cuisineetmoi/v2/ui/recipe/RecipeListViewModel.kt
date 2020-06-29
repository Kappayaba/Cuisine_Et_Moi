package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe

import android.os.AsyncTask
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.internal.LinkedTreeMap
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.LikeDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.RecipeDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.GetRoomUserLikesAsyncTask
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.UpdateRoomRecipeAsyncTask
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.response.LikeResponse
import com.starscience.cuisinietmoi.cuisineetmoi.v2.network.AppApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RecipeListViewModel(private val recipeDao: RecipeDao, private val likeDao: LikeDao) : BaseViewModel() {

    @Inject
    lateinit var appApi: AppApi
    lateinit var subscription: Disposable
    val isNetworkAvailable : MutableLiveData<Boolean> = MutableLiveData()

    val clickedRecipe: MutableLiveData<Recipe> = MutableLiveData()
    val recipeListAdapter = RecipeListAdapter() { recipe -> clickedRecipe.value = recipe }

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val swipeRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadRecipes() }

    init {
        swipeRefreshing.value = false
        loadRecipes()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadRecipes() {
        subscription = Observable.fromCallable{ recipeDao.all }
            .concatMap { dbRecipeList ->
                if(dbRecipeList.isEmpty()){
                    appApi.getRecipes().concatMap {apiList ->
                        recipeDao.saveRecipe(*apiList.values.toTypedArray())
                        Observable.just(apiList.values.toTypedArray().toList() as List<Recipe>)}
                }
                else {
                    Observable.just(dbRecipeList)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{ onRetrieveDataStart() }
            .subscribe(
                { recipes -> onRetrieveDataSuccess(recipes) },
                { e -> onRetrieveDataError(e) }
            )
    }

    private fun onRetrieveDataStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveDataFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveDataError(e: Throwable) {
        Log.w("Retrieve RECIPE ERROR", "$e")
        loadingVisibility.value = View.GONE
        errorMessage.value = R.string.recipes_error
    }

    @Suppress("UNCHECKED_CAST")
    private fun onRetrieveDataSuccess(recipes: List<Recipe>) {
        if(isNetworkAvailable.value == true){
            subscription =  appApi.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ result -> UpdateRoomRecipeAsyncTask(recipeDao, result.values.toList()).execute() }
        }

        subscription = Observable.fromCallable{ likeDao.all }
            .concatMap { dbLikeList ->
                if (dbLikeList.filter { likeResponse -> likeResponse.userId == FirebaseAuth.getInstance().currentUser?.uid.toString()} == listOf<LikeResponse>()){
                    appApi.getLikes(FirebaseAuth.getInstance().currentUser?.uid.toString()).concatMap{ apiLinkedTreeMap ->
                        val data = LikeResponse(FirebaseAuth.getInstance().currentUser?.uid.toString(), apiLinkedTreeMap)
                        likeDao.saveLike(data)
                    Observable.just(apiLinkedTreeMap)}
                }
                else{
                    var data = LinkedTreeMap<String, String>()
                    for(like in dbLikeList){
                        if(like.userId == FirebaseAuth.getInstance().currentUser?.uid.toString()){
                            data = like.recipeIds
                            break
                        }
                    }
                    Observable.just(data)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate{ onRetrieveDataFinish() }
            .subscribe(
                { likes ->  recipeListAdapter.updateRecipeList(recipes.reversed(), (likes as LinkedTreeMap<String, String>).keys.toList()) },
                { e -> Log.w("Retrieve LIKES ERROR", "$e") }
            )
    }

    fun searchWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            recipeListAdapter.filterRecipeList().filter(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            return
        }
    }

    val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        swipeRefreshing.value = true
        var likes = listOf<String>()
        Observable.fromCallable{ likeDao.getLikes(FirebaseAuth.getInstance().currentUser?.uid.toString()) }
            .subscribeOn(Schedulers.io())
            .subscribe{ response -> likes = response.recipeIds.keys.toList() }
        Log.w("TEST HAMZA", "$likes")
        appApi.getRecipes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate{swipeRefreshing.value = false}
            .subscribe { recipes ->
                UpdateRoomRecipeAsyncTask(recipeDao, recipes.values.toList()).execute(); recipeListAdapter.updateRecipeList(recipes.values.toList().reversed(), likes)
            }
    }
}

