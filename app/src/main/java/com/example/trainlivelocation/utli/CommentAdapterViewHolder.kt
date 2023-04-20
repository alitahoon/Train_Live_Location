package com.example.trainlivelocation.utli

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Post
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.PostCommentItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class CommentAdapterViewHolder(
    private val binding:PostCommentItemLayoutBinding,
    private val postListener: CommentListener
) :RecyclerView.ViewHolder(binding.root){
    fun bind(comment:PostCommentsResponseItem) {
        binding.comment = comment
        binding.listener=postListener
//        binding.postsBtnComment.setOnClickListener {
//            postListener.OnCommentClickListener()
//        }
    }
}