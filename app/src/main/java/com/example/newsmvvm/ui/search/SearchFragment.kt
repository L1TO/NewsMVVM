package com.example.newsmvvm.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.room.util.query
import com.example.newsmvvm.databinding.FragmentSearchBinding
import com.example.newsmvvm.ui.adapters.NewsAdapter
import com.example.newsmvvm.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val mBinding get() = _binding!!
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    private fun launchArticles(newText: String?) {
        var job: Job? = null
        job?.cancel()
        job = MainScope().launch {
            delay(2000)
            newText.toString().let {
                if (it.isNotEmpty()){
                    viewModel.getSearchNews(it)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                launchArticles(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                launchArticles(newText)
                return false
            }

        })
        viewModel.searchNewsLiveData.observe(viewLifecycleOwner, Observer { responce ->
            when (responce) {
                is Resource.Success -> {
                    mBinding.progressBar.visibility = View.GONE
                    adapter.articles = responce.data!!.articles
                }
                is Resource.Error -> {
                    mBinding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun initAdapter() {
        adapter = NewsAdapter()
        mBinding.recyclerSearch.adapter = adapter
    }

}