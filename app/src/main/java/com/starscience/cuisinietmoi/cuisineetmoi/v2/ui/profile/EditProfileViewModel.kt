package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile

import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.UserDao
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.UpdateRoomUserInfoAsyncTask
import com.starscience.cuisinietmoi.cuisineetmoi.v2.network.AppApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditProfileViewModel(private val userDao: UserDao) : BaseViewModel() {
    @Inject
    lateinit var appApi: AppApi
    lateinit var subscription: Any

    var newUserName: String = ""
    var newDescription: String = ""

    val editFinish: MutableLiveData<Boolean> = MutableLiveData()

    fun UpdateEdit(v: View){
        if(newDescription != "" && newUserName != ""){
            subscription = appApi.updateUsername(FirebaseAuth.getInstance().currentUser?.uid ?: "", newUserName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {},
                    {e -> onError(e)}
                )
            subscription = appApi.updateDescription(FirebaseAuth.getInstance().currentUser?.uid ?: "", newDescription)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {UpdateRoomUserInfoAsyncTask(userDao, FirebaseAuth.getInstance().currentUser?.uid ?: "", newUserName.trim(), newDescription.trim()).execute()},
                    {e -> onError(e)}
                )
        }
        editFinish.value = true
    }

    private fun onError(e: Throwable?) {
        Log.w("EROOR EDIT", "$e")
    }


    val newUsernameWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            newUserName = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            return
        }

    }

    val newDescriptionWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            newDescription = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            return
        }

    }
}