package com.example.newsfeed.data

data class Api(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)