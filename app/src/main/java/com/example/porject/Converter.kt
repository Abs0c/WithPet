package com.example.porject

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson

@ProvidedTypeConverter
class Converter(private val gson: Gson){
    @TypeConverter
    fun listToJson(value : List<Float>) : String? {
        return gson.toJson(value)
    }
    @TypeConverter
    fun jsonToList(value: String): List<String> {
        return gson.fromJson(value, Array<String>::class.java).toList()
    }
}