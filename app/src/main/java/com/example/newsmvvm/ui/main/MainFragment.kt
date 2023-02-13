package com.example.newsmvvm.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.newsmvvm.R
import com.example.newsmvvm.databinding.FragmentDetailsBinding
import com.example.newsmvvm.databinding.FragmentMainBinding
import com.example.newsmvvm.models.Article
import com.example.newsmvvm.ui.adapters.ArticleActionListener
import com.example.newsmvvm.ui.adapters.NewsAdapter
import com.example.newsmvvm.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer { responce ->
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
                    R.id.action_mainFragment_to_detailsFragment,
                    bundle
                )
            }

            override fun onArticleFavorite(article: Article) {
                TODO("Not yet implemented")
            }

            override fun onArticleShare(article: Article) {
                TODO("Not yet implemented")
            }

        })
        mBinding.recyclerView.adapter = adapter
    }

}