package com.example.newsfeed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeed.data.Article
import com.example.newsfeed.databinding.NewsItemBinding
import com.squareup.picasso.Picasso

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val newsList = ArrayList<Article>()

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int, article: Article)
    }

    fun setOnClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //add binding
        private val binding = NewsItemBinding.bind(itemView)

        fun bind(article: Article, listener: onItemClickListener) {
            // Перевірка, чи зображення не null перед встановленням
            article.urlToImage?.let { imageUrl ->
                Picasso.get().load(imageUrl).into(binding.im)
            }

            val title = article.title
            val description = article.description
            binding.mainTitle.text = title
            binding.tvDescription.text = description

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position], mListener)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun addNews(article: Article) {
        newsList.add(article)
        notifyDataSetChanged()
    }
}