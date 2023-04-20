package com.example.trainlivelocation.utli

import android.view.View
import com.example.domain.entity.Post
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.PostModelResponse

interface CommentListener {
     fun OnReportClickListener(post:PostCommentsResponseItem)
     fun OnDeleteClickListener(post:PostCommentsResponseItem)
     fun OnChatClickListener(post:PostCommentsResponseItem)
}