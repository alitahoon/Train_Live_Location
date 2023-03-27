package com.example.trainlivelocation.utli

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Post
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class PostAdapterViewHolder(
    private val binding:UserPostsRcvItemLayoutBinding,
    private val postListener: PostListener
) :RecyclerView.ViewHolder(binding.root){
    fun bind(post: Post) {
        binding.post = post
        binding.listener=postListener
    }
}