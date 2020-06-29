package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.login_register

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.User
import com.starscience.cuisinietmoi.cuisineetmoi.v2.network.AppApi
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.EMAIL_PATTERN
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.PASSWORD_PATTERN
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.STRING_LIKED
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

// Todo: Complete all users inputs
class RegisterViewModel : BaseViewModel() {
    @Inject
    lateinit var appApi: AppApi
    lateinit var subscription: Disposable

    val errorEmail: MutableLiveData<String> = MutableLiveData()
    val errorPassword: MutableLiveData<String> = MutableLiveData()
    val errorUsername: MutableLiveData<String> = MutableLiveData()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val validUserEnter: MutableLiveData<Boolean> = MutableLiveData()

    val auth = FirebaseAuth.getInstance()
    var email: ObservableField<String> = ObservableField()
    var password: ObservableField<String> = ObservableField()
    var username: ObservableField<String> = ObservableField()

    init {
        loadingVisibility.value = View.GONE
        validUserEnter.value = false
    }

    fun registerFirebase(v: View){
        validPassword(password.get() ?: "")
        validUsername()
        if(validEmail(email.get() ?: "") && validPassword(password.get() ?: "") && validUsername()) {
            loadingVisibility.value = View.VISIBLE
            auth.createUserWithEmailAndPassword(email.get().toString(), password.get().toString())
                .addOnSuccessListener { result -> createUser(result.user?.uid.toString()) }
                .addOnFailureListener{ onFirebaseAuthError() }
        }
    }

    private fun onFirebaseAuthError() {
        errorEmail.value = "Email already used"
        loadingVisibility.value = View.GONE
    }

    private fun createUser(firebaseId: String) {
        val user = User(firebaseId, username.get().toString(), "https://lenergeek.com/wp-content/uploads/2019/11/france-image-marche-energie-degrade-LEnergeek.jpg", "They called Coney Island, the playground of the world There was no place like it, in the whole world")
        subscription = appApi.like(firebaseId, "null", STRING_LIKED)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        subscription = appApi.createUser(firebaseId, user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate{doOnCreateUserFinish()}
            .subscribe(
                {e -> doOnCreateUserSuccess(e)},
                {e -> doOnCreateUserError(e)}
            )
    }

    private fun doOnCreateUserFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun doOnCreateUserError(e: Any) {
        Log.w("RegisterViewModel", "$e")
        return
    }

    private fun doOnCreateUserSuccess(e: Any) {
        validUserEnter.value = true
        return
    }

    //Text Watcher
    val emailWatcher: TextWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            if(email.get() != s.toString()){
                email.set(s.toString())
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            return
        }
    }

    val passwordWatcher: TextWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            if(password.get() != s.toString()){
                password.set(s.toString())
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            return
        }
    }

    val usernameWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            if(username.get() != s.toString()){
                username.set(s.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            return
        }

    }

    // Register field validation
    private fun validEmail(email: String): Boolean{
        if(email == "" || !EMAIL_PATTERN.matcher(email).matches()){
            errorEmail.value = "This email is not valid."
            return false
        }
        errorEmail.value = null
        return true
    }

    private fun validPassword(password: String): Boolean{
        if(password == "" || !PASSWORD_PATTERN.matcher(password).matches()){
            errorPassword.value = "The password is too weak (1 uppercase, 1 digit, at least 4 characters)."
            return false
        }
        errorPassword.value = null
        return true
    }

    private fun validUsername(): Boolean {
        if(username.get()?.replace("\\s".toRegex(), "") == "" || username.get() == null){
            errorUsername.value = "Username need to be field"
            return false
        }
        errorUsername.value = null
        return true
    }
}