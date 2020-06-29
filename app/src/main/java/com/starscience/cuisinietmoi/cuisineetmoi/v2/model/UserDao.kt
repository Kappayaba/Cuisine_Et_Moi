package com.starscience.cuisinietmoi.cuisineetmoi.v2.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @get:Query("SELECT * FROM user")
    val all: List<User>

    @Query("SELECT * FROM user WHERE id LIKE :userId")
    fun getUser(userId: String) : List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(vararg user: User)
}