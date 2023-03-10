package com.example.newsmvvm.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.newsmvvm.R
import com.example.newsmvvm.databinding.FragmentSearchBinding
import com.example.newsmvvm.models.Article
import com.example.newsmvvm.ui.ArticleViewModel
import com.example.newsmvvm.ui.adapters.ArticleActionListener
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
    private val viewModel by viewModels<ArticleViewModel>()
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
            delay(500)
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
/*                launchArticles(newText)*/
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
        adapter = NewsAdapter(object : ArticleActionListener {
            override fun onArticleDetails(article: Article) {
                val bundle = bundleOf("article" to article)
                findNavController().navigate(
                    R.id.action_searchFragment_to_detailsFragment,
                    bundle
                )
            }

            override fun onArticleShare(article: Article) {
                println("article.url")
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, article.url)
                val chooser = Intent.createChooser(intent, "Share link with your friends")
                startActivity(chooser)
            }

        })
        mBinding.recyclerSearch.adapter = adapter
    }

}