package com.example.retrofitwithdagger_hilt.model

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)