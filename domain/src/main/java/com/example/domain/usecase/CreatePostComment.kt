package com.example.domain.usecase

import Resource
import com.example.domain.entity.CommentRequest
import com.example.domain.entity.CommentResponse
import com.example.domain.repo.UserRepo

class CreatePostComment(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        commentRequset: CommentRequest,
        result: (Resource<CommentResponse>) -> Unit
    ) = userRepo.createPostComment(commentRequset,result)
}