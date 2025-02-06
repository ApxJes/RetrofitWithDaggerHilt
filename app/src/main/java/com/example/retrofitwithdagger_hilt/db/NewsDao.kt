package com.example.retrofitwithdagger_hilt.db

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.retrofitwithdagger_hilt.model.Article
import com.example.retrofitwithdagger_hilt.model.NewsResponse

interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNews(article: Article): Long

    @Query("SELECT * FROM news_table ORDER BY id DESC")
    fun getSaveNews(): LiveData<List<NewsResponse>>

    @Delete
    suspend fun deleteNews(article: Article)
}