package com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database

import androidx.room.TypeConverter
import com.google.gson.internal.LinkedTreeMap

class Converters {

    @TypeConverter
    fun fromLinkedTreeMap(linkedTreeMap: LinkedTreeMap<String, String>): String{
        return linkedTreeMap.keys.toList().joinToString(separator = ",")
    }

    @TypeConverter
    fun toLinkedTreeMap(string: String) : LinkedTreeMap<String, String>{
        val list = string.split(",").map { it.trim() }
        val linkedTreeMap = LinkedTreeMap<String, String>()
        for (s in list){
            linkedTreeMap[s] = "IS_LIKED"
        }
        return linkedTreeMap
    }

    @TypeConverter
    fun fromList(list: List<String>) : String{
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(string: String) : List<String>{
        return string.split(",").map { it.trim() }
    }
}