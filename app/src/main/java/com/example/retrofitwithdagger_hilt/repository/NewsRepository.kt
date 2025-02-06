package com.example.retrofitwithdagger_hilt.repository

import com.example.retrofitwithdagger_hilt.api.NewsApi
import javax.inject.Inject

class NewsRepository
@Inject constructor(
    private val newsApi: NewsApi
){
    suspend fun getBreakingNews(countryCode: String, pageSize: Int) =
        newsApi.getBreakingNews(countryCode, pageSize)

    suspend fun getSearchNews(searchQuery: String, pageSize: Int) =
        newsApi.getSearchNews(searchQuery, pageSize)
}