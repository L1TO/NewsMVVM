package com.example.newsmvvm.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.newsmvvm.databinding.FragmentDetailsBinding
import com.example.newsmvvm.ui.ArticleViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val mBinding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<ArticleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        mBinding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url!!)
        }

        fab.setOnClickListener {
            viewModel.saveFavoriteArticle(article)
            Snackbar.make(view, "Article saved", Snackbar.LENGTH_SHORT).show()
        }
    }

}