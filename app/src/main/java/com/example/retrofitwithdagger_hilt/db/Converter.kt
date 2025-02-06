package com.example.retrofitwithdagger_hilt.db

import androidx.room.TypeConverter
import com.example.retrofitwithdagger_hilt.model.Source

class Converter {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name, name)
    }
}