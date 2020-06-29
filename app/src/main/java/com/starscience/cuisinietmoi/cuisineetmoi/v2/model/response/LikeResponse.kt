package com.starscience.cuisinietmoi.cuisineetmoi.v2.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.internal.LinkedTreeMap
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.Converters

@Entity
data class LikeResponse(
    @field:PrimaryKey
    val userId: String,
    @TypeConverters(Converters::class)
    val recipeIds: LinkedTreeMap<String, String>
)