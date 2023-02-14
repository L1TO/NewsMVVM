package com.example.newsmvvm.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmvvm.data.api.NewsRepository
import com.example.newsmvvm.models.Article
import com.example.newsmvvm.models.NewsResponse
import com.example.newsmvvm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    val newsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage = 1

    init {
        getNews("ua")
    }

    fun getSearchNews(query: String) =
        viewModelScope.launch {
            searchNewsLiveData.postValue(Resource.Loading())
            val response = repository.searchNews(query, pageNumber = newsPage)
            if (response.isSuccessful) {
                response.body().let {
                    searchNewsLiveData.postValue(Resource.Success(data = it))

                }
            } else {
                searchNewsLiveData.postValue(Resource.Error(message = response.message()))
            }
        }


    fun getNews(countryCode: String) =
        viewModelScope.launch {
            newsLiveData.postValue(Resource.Loading())
            val response = repository.getNews(countryCode, newsPage)
            if (response.isSuccessful){
                response.body().let {
                    newsLiveData.postValue(Resource.Success(data = it))

                }
            } else {
                newsLiveData.postValue(Resource.Error(message = response.message()))
            }
        }

    fun getSavedArticles() = repository.getFavoriteArticles()

    fun saveFavoriteArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavorite(article)
        }
    }

    fun deleteFavoriteArticle(article: Article){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromFavorite(article)
        }
    }

}