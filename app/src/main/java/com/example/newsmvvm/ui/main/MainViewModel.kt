package com.example.newsmvvm.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmvvm.data.api.NewsRepository
import com.example.newsmvvm.models.Article
import com.example.newsmvvm.models.NewsResponse
import com.example.newsmvvm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    val newsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage = 1

    init {
        getNews("ua")
    }
    private fun getNews(countryCode: String) =
        viewModelScope.launch {
            newsLiveData.postValue(Resource.Loading())
            val response = newsRepository.getNews(countryCode, newsPage)
            if (response.isSuccessful){
                response.body().let {
                    newsLiveData.postValue(Resource.Success(data = it))

                }
            } else {
                newsLiveData.postValue(Resource.Error(message = response.message()))
            }
        }

}