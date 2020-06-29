package com.starscience.cuisinietmoi.cuisineetmoi.v2.model

import androidx.room.*
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.response.LikeResponse

@Dao
interface LikeDao {
    @get:Query("SELECT * FROM `likeresponse`")
    val all : List<LikeResponse>

    @Query("SELECT * FROM likeresponse WHERE userId LIKE :userid ")
    fun getLikes(userid: String) : LikeResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLike(vararg like: LikeResponse)

    @Delete
    fun deleteLike(vararg like: LikeResponse)
}