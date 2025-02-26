package com.shivam.newsappshorts.utility.network

import com.shivam.newsappshorts.fragments.home.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {
    @GET("v2/everything")
    suspend fun getNewsPage(
        @Query("apiKey") apiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response<NewsResponse>

    @GET
    suspend fun getNewsList(@Url url:String):Response<NewsResponse?>

}