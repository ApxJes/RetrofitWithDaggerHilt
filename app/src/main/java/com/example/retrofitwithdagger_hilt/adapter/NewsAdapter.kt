package com.example.retrofitwithdagger_hilt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitwithdagger_hilt.R
import com.example.retrofitwithdagger_hilt.model.Article
import javax.inject.Inject

class NewsAdapter
@Inject constructor(): RecyclerView.Adapter<NewsAdapter.ItemViewHolder>() {

    lateinit var image: ImageView
    lateinit var title: TextView
    lateinit var source: TextView
    lateinit var description: TextView
    lateinit var dateAndTime: TextView

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallBack = object: DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.news_item,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val article = differ.currentList[position]

        image = holder.itemView.findViewById(R.id.imvArticleImage)
        title = holder.itemView.findViewById(R.id.txvArticleTitle)
        description = holder.itemView.findViewById(R.id.txvArticleDescription)
        source = holder.itemView.findViewById(R.id.txvArticleSource)
        dateAndTime = holder.itemView.findViewById(R.id.txvArticleDateAndTime)

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(image)
            title.text = article.title
            description.text = article.description
            source.text = article.source?.name
            dateAndTime.text = article.publishedAt

            setOnClickListener {
                onItemClick?.let {
                    it(article)
                }
            }
        }
    }

    private var onItemClick:((Article) -> Unit)? = null
    fun onItemClickListener(listener: (Article) -> Unit) {
        onItemClick = listener
    }

}