package com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.welcome_fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.starscience.cuisinietmoi.cuisineetmoi.v2.MainActivity
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.databinding.FragmentLoginBinding
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.login_register.LoginViewModel

class LoginActivity : AppCompatActivity(){
    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginViewModel
    var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Login)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer { value -> if(value == null) hideError() else showError(value)})
        viewModel.isLogged.observe(this, Observer { value -> if(value == true) login() })
        binding.viewModel = viewModel
    }

    private fun hideError() {
        snackbar?.dismiss()
    }

    private fun showError(value: String) {
        snackbar = Snackbar.make(binding.root, value, Snackbar.LENGTH_LONG)
        snackbar?.show()
    }

    private fun login() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun toRegister(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
    }

}
