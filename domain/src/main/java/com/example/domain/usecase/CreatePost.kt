package com.example.domain.usecase

import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.domain.repo.UserRepo
import retrofit2.Response

class CreatePost (private val userRepo: UserRepo){

    suspend operator fun invoke(post: Post): Response<PostModelResponse> = userRepo.createPost(post)

}