package com.starscience.cuisinietmoi.cuisineetmoi.v2.component

import com.starscience.cuisinietmoi.cuisineetmoi.v2.module.NetworkModule
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.login_register.RegisterViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.EditProfileViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.LastPublishedViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.UserInfoViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(recipeListViewModel: RecipeListViewModel)
    fun inject(recipeViewModel : RecipeViewModel)
    fun inject(registerViewModel: RegisterViewModel)
    fun inject(addRecipeViewModel: AddRecipeViewModel)
    fun inject(favoriteListVIewModel: RecipeFavoriteListVIewModel)
    fun inject(userInfoViewModel: UserInfoViewModel)
    fun inject(lastPublishedViewModel: LastPublishedViewModel)
    fun inject(editProfileViewModel: EditProfileViewModel)

    @Component.Builder
    interface Builder{
        fun build() : ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}