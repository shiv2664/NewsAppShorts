package com.shivam.newsappshorts.repository

import android.app.Application

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.shivam.newsappshorts.utility.network.ApiInterface
import com.shivam.newsappshorts.fragments.home.model.Article
import com.shivam.newsappshorts.fragments.home.paging_source.PagingSourceMovies
import com.shivam.newsappshorts.utility.local.XDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyNewsRepository @Inject constructor(private val apiInterface: ApiInterface, private val dao: XDao,private val application: Application) {

    fun getData(apiKey: String,query: String) = Pager(
        config = PagingConfig(5, maxSize = 50, enablePlaceholders = true),
        pagingSourceFactory = { PagingSourceMovies(apiInterface,apiKey,query,application) }).flow

    fun getBookmarksLive(): LiveData<List<Article>> {
        return dao.getBookmarksLive()
    }

    fun getBookmarkIdsLiveData(): LiveData<List<String>> {
        return dao.getBookmarkIdsLiveData()
    }

    suspend fun getBookmark(title: String): Article? = withContext(Dispatchers.IO) {
        return@withContext dao.checkBookmark(title)
    }

    suspend fun removeBookmark(title: String) {
        dao.deleteBookmark(title)

    }

    suspend fun insertBookmark(item: Article?) {
        if (item != null) {
            dao.insertBookmark(item)
        }

    }




}