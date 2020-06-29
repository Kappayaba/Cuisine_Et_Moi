package com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.welcome_fragment


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.databinding.FragmentRegisterBinding
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.login_register.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Register)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_register)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        viewModel.validUserEnter.observe(this, Observer { value -> if(value == true) finish()})
        binding.viewModel = viewModel
    }

    fun toLogin(view: View){
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left)
    }

}