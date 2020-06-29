package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.User
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.UserDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.network.AppApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserInfoViewModel(private val userDao: UserDao) : BaseViewModel() {
    @Inject
    lateinit var appApi: AppApi
    lateinit var subscription: Disposable

    val username: MutableLiveData<String> = MutableLiveData()
    val userProfileImage: MutableLiveData<String> = MutableLiveData()
    val userDescription: MutableLiveData<String> = MutableLiveData()

    val loadingVisibility: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadingVisibility.value = true
        loadUser()
    }

    private fun loadUser() {
        subscription = Observable.fromCallable{ userDao.getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "") }
            .concatMap { dbUser ->
                if(dbUser.isEmpty()){
                    appApi.getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                        .concatMap { apiUser -> userDao.saveUser(apiUser); Observable.just(apiUser)}
                }
                else{
                    Observable.just(dbUser[0])
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onRetrieveUserSuccess(result)},
                { e -> onRetrieveUserError(e)}
            )
    }

    private fun onRetrieveUserSuccess(result: User) {
        username.value = result.username
        userProfileImage.value = result.profil_image
        userDescription.value = result.user_description
        loadingVisibility.value = false
    }

    private fun onRetrieveUserError(e: Throwable?) {
        Log.w("USER INFO ERROR", "$e")
        loadingVisibility.value = false
    }
}