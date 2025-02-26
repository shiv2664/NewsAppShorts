package com.shivam.newsappshorts.fragments.home.paging_source

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shivam.newsappshorts.fragments.home.model.Article
import com.shivam.newsappshorts.utility.Utility
import com.shivam.newsappshorts.utility.network.ApiInterface
import java.io.IOException
import javax.inject.Inject


class PagingSourceMovies @Inject constructor(
    val apiInterface: ApiInterface,
    private val apiKey: String,
    private val query: String,
    private  var application: Context
) : PagingSource<Int, Article>() {
    private var prevSearchKey = ""

    val defaultIndex: Int = 1
    val url1 = "http://www.omdbapi.com/?apikey=84c8ffe674244229b62799d33774504a&q=India&page=$"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        if (!application.isInternetAvailable()) {
            return LoadResult.Error(IOException("No internet connection"))
        }

        var page = params.key ?: 1
        if (prevSearchKey != query) {
            page = defaultIndex
            prevSearchKey = query
        } else {
            page = params.key ?: defaultIndex
        }

        val response = apiInterface.getNewsList("https://newsapi.org/v2/everything?apiKey=$apiKey&q=$query&page=$page&pageSize=10")

        return LoadResult.Page(
            data = response.body()?.articles ?:emptyList(),
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (response.body()?.articles?.isEmpty() == true) null else page + 1
        )

    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    fun Context.isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

}