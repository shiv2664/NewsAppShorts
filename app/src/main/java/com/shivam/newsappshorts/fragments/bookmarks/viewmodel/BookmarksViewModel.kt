package com.shivam.newsappshorts.fragments.bookmarks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.shivam.newsappshorts.fragments.home.model.Article
import com.shivam.newsappshorts.repository.MyNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(private val repository: MyNewsRepository): ViewModel() {

    val bookmarkFeedLiveData = MutableLiveData<List<Article>?>()

    val bookmarkIdsLiveData: LiveData<List<String>> = repository.getBookmarkIdsLiveData()

    init {
        // Observe the LiveData from the repository
        viewModelScope.launch {
            repository.getBookmarksLive().observeForever {
                bookmarkFeedLiveData.value = it
            }
        }
    }

    suspend fun getBookmark(title: String): Article? = viewModelScope.async {
        return@async repository.getBookmark(title)
    }.await()


    fun removeBookmark(id: String) {
        viewModelScope.launch {
            repository.removeBookmark(id)
        }
    }

    fun insertBookmark(item: Article?) {
        viewModelScope.launch {
            repository.insertBookmark(item)
        }
    }


}