package com.example.newsmvvm.data.api

import com.example.newsmvvm.data.database.ArticleDao
import com.example.newsmvvm.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao) {
    suspend fun getNews(countryCode: String, pageNumber: Int) = newsService.getHeadlines(countryCode, pageNumber)
    suspend fun searchNews(query: String, pageNumber: Int) = newsService.getEverything(query, pageNumber)

    fun getFavoriteArticles() = articleDao.getAllArticles()

    suspend fun addToFavorite(article: Article) = articleDao.insert(article)

    suspend fun deleteFromFavorite(article: Article) = articleDao.delete(article)

}