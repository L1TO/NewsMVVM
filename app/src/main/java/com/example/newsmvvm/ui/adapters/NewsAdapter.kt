package com.example.newsmvvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsmvvm.R
import com.example.newsmvvm.databinding.ItemArticleLayoutBinding
import com.example.newsmvvm.models.Article

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

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        with(holder.binding) {
            Glide.with(root)
                .load(article.urlToImage)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(ivArticleImage)
            tvArticleSource.text = article.source.name
            tvArticleTitle.text = article.title
            tvArticleDate.text = article.publishedAt.substring(0, 10)
        }

    }
}