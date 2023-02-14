package com.example.newsmvvm.ui.adapters

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsmvvm.R
import com.example.newsmvvm.databinding.ItemArticleLayoutBinding
import com.example.newsmvvm.models.Article
import kotlinx.android.synthetic.main.item_article_layout.*
import kotlinx.coroutines.withContext
import java.util.logging.Handler

interface ArticleActionListener {
    fun onArticleDetails(article: Article)

    fun onArticleShare(article: Article)
}

class NewsDiffCallback(
    private val oldList: List<Article>,
    private val newList: List<Article>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldArticle = oldList[oldItemPosition]
        val newArticle = newList[newItemPosition]
        return oldArticle.url == newArticle.url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldArticle = oldList[oldItemPosition]
        val newArticle = newList[newItemPosition]
        return oldArticle == newArticle
    }
}

class NewsAdapter(
    private val articleActionListener: ArticleActionListener
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(), View.OnClickListener {

    private var count = 0

    var articles: List<Article> = emptyList()
        set(value) {
            val diffCallback = NewsDiffCallback(field, value)
            val diffresult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffresult.dispatchUpdatesTo(this)
        }

    class ViewHolder(val binding: ItemArticleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onClick(view: View) {
        val article = view.tag as Article

        when (view.id) {
            R.id.iv_share -> {
                articleActionListener.onArticleShare(article)
            }
            else -> {
                articleActionListener.onArticleDetails(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleLayoutBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)

        return ViewHolder(binding)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        with(holder.binding) {
            holder.itemView.tag = article
            ivShare.tag = article
            Glide.with(root)
                .load(article.urlToImage)
                .placeholder(R.drawable.no_image)
                .into(ivArticleImage)
            tvArticleTitle.text = article.title
            tvArticleDate.text = article.publishedAt?.substring(0, 10)
            tvArticleAuthor.text = article.author
        }
    }
}