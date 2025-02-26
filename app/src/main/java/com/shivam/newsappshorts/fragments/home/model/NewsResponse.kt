package com.shivam.newsappshorts.fragments.home.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

@Entity(tableName = "article")
data class Article(
    @PrimaryKey()
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)