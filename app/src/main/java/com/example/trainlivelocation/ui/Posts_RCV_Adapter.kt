package com.example.trainlivelocation.ui

import com.example.domain.entity.Post
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class Posts_RCV_Adapter(
     private val postList: ArrayList<Post>, private val listener:PostListener,
    override val layoutId: Int
) :
    PostBaseAdapter<UserPostsRcvItemLayoutBinding, Post>(postList) {
    override fun bind(postBinding: UserPostsRcvItemLayoutBinding, item: Post) {
        postBinding.apply {
            var postItem = item
            var postListener = listener
            executePendingBindings()
        }
    }
}