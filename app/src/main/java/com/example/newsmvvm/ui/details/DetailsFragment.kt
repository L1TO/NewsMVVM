package com.example.newsmvvm.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsmvvm.R
import com.example.newsmvvm.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val mBinding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()

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
        with(article) {
            Glide.with(this@DetailsFragment)
                .load(article.urlToImage)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(mBinding.ivHeaderImage)
            mBinding.articleDescriptionTitle.text = article.title
            mBinding.articleDetailsDescriptionText.text = article.description
            mBinding.ivHeaderImage.clipToOutline = true
            mBinding.btnOpenArticle.setOnClickListener {
                try {
                    Intent()
                        .setAction(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse(takeIf { URLUtil.isValidUrl(article.url) }
                            ?.let {
                                article.url
                            } ?: "https://google.com"))
                        .let {
                            ContextCompat.startActivity(requireContext(), it, null)
                        }

                } catch (e: java.lang.Exception) {
                    Toast.makeText(
                        requireContext(),
                        "The device doesnt have any browser to view the article!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}