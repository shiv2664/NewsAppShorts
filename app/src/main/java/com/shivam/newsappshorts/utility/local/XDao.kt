package com.shivam.newsappshorts.utility.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivam.newsappshorts.fragments.home.model.Article

@Dao
interface XDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(item: Article)

    @Query("SELECT * from article")
    fun getBookmarks(): List<Article>

    @Query("DELETE FROM article WHERE title = :title")
    suspend fun deleteBookmark(title: String)

    @Query("SELECT * FROM article")
    fun getBookmarksLive(): LiveData<List<Article>>

    @Query("SELECT * FROM article WHERE title = :title")
    suspend fun checkBookmark(title: String): Article?

    @Query("SELECT title FROM article")
    fun getBookmarkIdsLiveData(): LiveData<List<String>>

}

