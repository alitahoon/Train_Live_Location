package com.example.trainlivelocation.utli

import android.view.View
import com.example.domain.entity.GetNewsResponseItem
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse

interface NewsListener {
     fun OnNewsClickListener(news:GetNewsResponseItem)
}