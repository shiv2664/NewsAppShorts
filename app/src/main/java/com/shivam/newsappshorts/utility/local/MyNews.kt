package com.shivam.newsappshorts.utility.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shivam.newsappshorts.fragments.home.model.Article

@Database(
    entities = [
        Article::class
    ],
    version = 1,
    exportSchema = false
)

abstract class MyDatabase : RoomDatabase() {
    abstract fun xDao(): XDao
}