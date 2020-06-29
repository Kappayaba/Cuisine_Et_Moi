package com.starscience.cuisinietmoi.cuisineetmoi.v2.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @field:PrimaryKey
    val id: String,
    var username: String,
    var profil_image: String,
    var user_description: String
)