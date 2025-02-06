package com.example.retrofitwithdagger_hilt.repository

import com.example.retrofitwithdagger_hilt.api.NewsApi
import com.example.retrofitwithdagger_hilt.db.NewsDao
import com.example.retrofitwithdagger_hilt.model.Article
import javax.inject.Inject

class NewsRepository
@Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
){
    suspend fun getBreakingNews(countryCode: String, pageSize: Int) =
        newsApi.getBreakingNews(countryCode, pageSize)

    suspend fun getSearchNews(searchQuery: String, pageSize: Int) =
        newsApi.getSearchNews(searchQuery, pageSize)

    suspend fun saveNews(article: Article) = newsDao.saveNews(article)

    fun getSaveNews() = newsDao.getSaveNews()

    suspend fun deleteNews(article: Article) = newsDao.deleteNews(article)
}