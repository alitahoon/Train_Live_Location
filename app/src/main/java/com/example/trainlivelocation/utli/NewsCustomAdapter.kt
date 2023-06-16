package com.example.trainlivelocation.utli

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.GetNewsResponseItem
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.NewsRcvItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class NewsCustomAdapter(private val newsListener: NewsListener) :
    RecyclerView.Adapter<NewsAdapterViewHolder>(), BindableAdapter<ArrayList<GetNewsResponseItem>> {

    private var binding: NewsRcvItemLayoutBinding? = null
    lateinit var newsArraylist: ArrayList<GetNewsResponseItem>
    var newsList = emptyList<GetNewsResponseItem>()

    public fun updateData(postList: ArrayList<GetNewsResponseItem>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapterViewHolder {
        binding = NewsRcvItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsAdapterViewHolder(binding!!, newsListener)
    }

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: NewsAdapterViewHolder, position: Int) {
        val news = newsList.get(position)
        holder.bind(news)
    }

    override fun setData(data: ArrayList<GetNewsResponseItem>) {
        this.newsList = data
        notifyDataSetChanged()
    }
}
