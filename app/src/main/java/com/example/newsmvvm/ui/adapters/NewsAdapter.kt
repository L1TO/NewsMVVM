package com.example.newsmvvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsmvvm.databinding.ItemArticleLayoutBinding
import com.example.newsmvvm.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var articles: List<Article> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
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
            Glide.with(root).load(article.urlToImage).into(ivArticleImage)
            tvArticleSource.text = article.source.name
            tvArticleTitle.text = article.title
            tvArticleDate.text = article.publishedAt.substring(0, 10)
        }

    }
}