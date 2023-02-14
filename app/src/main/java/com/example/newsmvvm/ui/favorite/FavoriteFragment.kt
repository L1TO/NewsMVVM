package com.example.newsmvvm.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsmvvm.R
import com.example.newsmvvm.databinding.FragmentFavoriteBinding
import com.example.newsmvvm.models.Article
import com.example.newsmvvm.ui.ArticleViewModel
import com.example.newsmvvm.ui.adapters.ArticleActionListener
import com.example.newsmvvm.ui.adapters.NewsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_article_layout.*

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val mBinding get() = _binding!!
    private val viewModel by viewModels<ArticleViewModel>()
    private val adapter = NewsAdapter(object : ArticleActionListener {
        override fun onArticleDetails(article: Article) {
            val bundle = bundleOf("article" to article)
            val exstras = FragmentNavigatorExtras(iv_article_image to "image_big")
            findNavController().navigate(
                R.id.action_favoriteFragment_to_detailsFragment,
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        mBinding.recyclerFavorite.adapter = adapter
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.articles[position]
                viewModel.deleteFavoriteArticle(article)
                Snackbar.make(view, "Delete successfuly", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        viewModel.saveFavoriteArticle(article)
                    }
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(mBinding.recyclerFavorite)
        }

        viewModel.getSavedArticles().observe(viewLifecycleOwner) {
            adapter.articles = it
        }
    }
}