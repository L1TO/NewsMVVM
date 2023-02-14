package com.example.newsmvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsmvvm.models.Article

@Database(entities = [Article::class], version = 1, exportSchema = true)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

}