package com.example.newsmvvm.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmvvm.data.api.NewsRepository
import com.example.newsmvvm.models.NewsResponse
import com.example.newsmvvm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    val searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getSearchNews("")
    }

    fun getSearchNews(query: String) =
        viewModelScope.launch {
            searchNewsLiveData.postValue(Resource.Loading())
            val response = repository.searchNews(query, pageNumber = searchNewsPage)
            if (response.isSuccessful) {
                response.body().let {
                    searchNewsLiveData.postValue(Resource.Success(data = it))

                }
            } else {
                searchNewsLiveData.postValue(Resource.Error(message = response.message()))
            }
        }

}