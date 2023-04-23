package com.example.trainlivelocation.utli

import android.view.View
import com.example.domain.entity.Message
import com.example.domain.entity.Post
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.PostModelResponse

interface MessageListener {
     fun OnDeleteClickListener(message:Message)
     fun OninboxItemClickListener(message:Message)
}