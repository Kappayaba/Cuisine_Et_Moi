package com.starscience.cuisinietmoi.cuisineetmoi.v2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Like(
    @field:PrimaryKey
    val userId: String,
    val recipeId: String?
)