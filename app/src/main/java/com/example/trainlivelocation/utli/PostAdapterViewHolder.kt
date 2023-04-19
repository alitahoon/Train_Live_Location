package com.example.trainlivelocation.utli

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class PostAdapterViewHolder(
    private val binding:UserPostsRcvItemLayoutBinding,
    private val postListener: PostListener
) :RecyclerView.ViewHolder(binding.root){
    fun bind(post:PostModelResponse) {
        binding.post = post
        binding.listener=postListener
//        binding.postsBtnComment.setOnClickListener {
//            postListener.OnCommentClickListener()
//        }
    }
}