package com.starscience.cuisinietmoi.cuisineetmoi.v2.utils

import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import java.util.regex.Pattern

const val BASE_URL = "https://cuisine-et-moi.firebaseio.com/"
const val PICK_IMAGE_REQUEST = 1
const val STRING_LIKED = "IS_LIKED"
val DRAWABLE_ICON_LIST = listOf(R.drawable.cutting_board, R.drawable.kitchen_utensils,  R.drawable.kitchen_utensils_2, R.drawable.knife)

val EMAIL_PATTERN: Pattern = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
val PASSWORD_PATTERN: Pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{4,255}$")

const val EXTRA_RECIPE: String = "recipe"
const val EXTRA_STEPS: String = "steps"
const val REQ_CODE_SPEECH_INPUT = 2