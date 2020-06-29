package com.starscience.cuisinietmoi.cuisineetmoi.v2.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @get:Query("SELECT * FROM recipe")
    val all : List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRecipe(vararg recipe: Recipe)
}