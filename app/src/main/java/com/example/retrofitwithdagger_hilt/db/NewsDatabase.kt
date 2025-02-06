package com.example.retrofitwithdagger_hilt.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.retrofitwithdagger_hilt.model.Article
import javax.inject.Inject

@Database(entities = [Article::class], version = 1)
@TypeConverters(Converter::class)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
}