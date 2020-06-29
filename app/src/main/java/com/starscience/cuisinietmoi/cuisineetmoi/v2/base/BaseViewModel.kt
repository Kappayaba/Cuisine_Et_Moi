package com.starscience.cuisinietmoi.cuisineetmoi.v2.base

import androidx.lifecycle.ViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.component.DaggerViewModelInjector
import com.starscience.cuisinietmoi.cuisineetmoi.v2.module.NetworkModule
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.login_register.RegisterViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.EditProfileViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.LastPublishedViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.UserInfoViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe.*

abstract class BaseViewModel : ViewModel() {
    private val injector = DaggerViewModelInjector.builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    private fun inject() {
        when(this){
            is RecipeListViewModel -> injector.inject(this)
            is RecipeViewModel -> injector.inject(this)
            is RegisterViewModel -> injector.inject(this)
            is AddRecipeViewModel -> injector.inject(this)
            is RecipeFavoriteListVIewModel -> injector.inject(this)
            is UserInfoViewModel -> injector.inject(this)
            is LastPublishedViewModel -> injector.inject(this)
            is EditProfileViewModel -> injector.inject(this)
        }
    }
}