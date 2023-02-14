package com.example.newsmvvm.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.newsmvvm.R
import com.example.newsmvvm.databinding.FragmentMainBinding
import com.example.newsmvvm.models.Article
import com.example.newsmvvm.ui.ArticleViewModel
import com.example.newsmvvm.ui.adapters.ArticleActionListener
import com.example.newsmvvm.ui.adapters.NewsAdapter
import com.example.newsmvvm.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_article_layout.*
import kotlinx.android.synthetic.main.item_article_layout.view.*
import java.net.URI
import java.net.URL

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    private val viewModel by viewModels<ArticleViewModel>()
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
                val exstras = FragmentNavigatorExtras(iv_article_image to "image_big")
                findNavController().navigate(
                    R.id.action_mainFragment_to_detailsFragment,
                    bundle,
                    null,
                    exstras
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
        mBinding.recyclerView.adapter = adapter
    }

}