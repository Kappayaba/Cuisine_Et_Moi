package com.starscience.cuisinietmoi.cuisineetmoi.v2.component

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.profile.LastPublished
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.AppDatabase
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.EditProfileViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.LastPublishedViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.UserInfoViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe.AddRecipeViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe.RecipeFavoriteListVIewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe.RecipeListViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RecipeListViewModel::class.java)){
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "cuisine_et_moi").build()
            @Suppress("UNCHECKED_CAST")
            return RecipeListViewModel(db.recipeDao(), db.likeDao()) as T
        }

        if(modelClass.isAssignableFrom(RecipeFavoriteListVIewModel::class.java)){
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "cuisine_et_moi").build()
            @Suppress("UNCHECKED_CAST")
            return RecipeFavoriteListVIewModel(db.recipeDao(), db.likeDao()) as T
        }

        if(modelClass.isAssignableFrom(AddRecipeViewModel::class.java)){
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "cuisine_et_moi").build()
            @Suppress("UNCHECKED_CAST")
            return AddRecipeViewModel(db.recipeDao()) as T
        }

        if (modelClass.isAssignableFrom(LastPublishedViewModel::class.java)){
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "cuisine_et_moi").build()
            @Suppress("UNCHECKED_CAST")
            return LastPublishedViewModel(db.recipeDao(), db.likeDao()) as T
        }

        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)){
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "cuisine_et_moi").build()
            @Suppress("UNCHECKED_CAST")
            return UserInfoViewModel(db.userDao()) as T
        }

        if(modelClass.isAssignableFrom(EditProfileViewModel::class.java)){
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "cuisine_et_moi").build()
            @Suppress("UNCHECKED_CAST")
            return EditProfileViewModel(db.userDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}