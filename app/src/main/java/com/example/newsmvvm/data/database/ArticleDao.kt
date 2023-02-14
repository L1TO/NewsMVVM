package com.example.newsmvvm.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsmvvm.models.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article)
}