package com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.*
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.response.LikeResponse

@Database(entities = [Recipe::class, User::class, LikeResponse::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao() : RecipeDao
    abstract fun userDao() : UserDao
    abstract fun likeDao() : LikeDao
}