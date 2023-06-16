package com.example.trainlivelocation.utli

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.GetNewsResponseItem
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.NewsRcvItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class NewsAdapterViewHolder(
    private val binding: NewsRcvItemLayoutBinding,
    private val newsListener: NewsListener
) :RecyclerView.ViewHolder(binding.root){
    fun bind(news:GetNewsResponseItem) {
        binding.news = news
        binding.listener=newsListener
//        binding.postsBtnComment.setOnClickListener {
//            postListener.OnCommentClickListener()
//        }
    }
}