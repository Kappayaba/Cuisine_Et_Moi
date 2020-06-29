package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.login_register

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseViewModel
import java.lang.Exception

class LoginViewModel : BaseViewModel() {
    private val auth = FirebaseAuth.getInstance()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val isLogged: MutableLiveData<Boolean> = MutableLiveData()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    init {
        isLogged.value = auth.currentUser != null
        loadingVisibility.value = View.GONE
    }

    fun loginFirebase(v: View){
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
        auth.signInWithEmailAndPassword(email.value ?: "any", password.value ?: "any")
            .addOnSuccessListener { signInSuccess() }
            .addOnFailureListener { e -> signInError(e) }
    }

    private fun signInError(e: Exception) {
        loadingVisibility.value = View.GONE
        errorMessage.value = e.message
    }

    private fun signInSuccess() {
        isLogged.value = true
        loadingVisibility.value = View.GONE
    }

    //Text watcher
    fun emailWatcher() : TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if(email.value != s.toString()){
                email.value = s.toString()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            return
        }
    }

    fun passwordWatcher() : TextWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            if (password.value != s.toString()){
                password.value = s.toString()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            return
        }
    }
}