package com.example.newsmvvm.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmvvm.data.api.NewsService
import com.example.newsmvvm.data.api.TestRepo
import com.example.newsmvvm.models.NewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val testRepo: TestRepo) : ViewModel() {

    private var _all = MutableLiveData<NewsResponse>()
    val all: LiveData<NewsResponse>
        get() = _all

    init {
        getAll()
    }

    fun getAll() = viewModelScope.launch {
        testRepo.getAll().let {
            if (it.isSuccessful) {
                _all.postValue(it.body())
            } else {
                Log.d("checkData", "${it.errorBody()}")
            }
        }
    }
}