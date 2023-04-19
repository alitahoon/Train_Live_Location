package com.example.trainlivelocation.utli

import android.view.View
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse

interface PostListener {
     fun OnCommentClickListener( post: PostModelResponse)
     fun OnReportClickListener(post:PostModelResponse)
     fun OnDeleteClickListener(post:PostModelResponse)
     fun OnSettingClickListener(post:PostModelResponse)
}