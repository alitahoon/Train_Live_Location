package com.example.trainlivelocation.utli

import com.example.domain.entity.Post

interface PostListener {
    public fun OnCommentClickListener(post: Post)
    public fun OnReportClickListener(post:Post)
    public fun OnDeleteClickListener(post:Post)
    public fun OnSettingClickListener(post:Post)
}