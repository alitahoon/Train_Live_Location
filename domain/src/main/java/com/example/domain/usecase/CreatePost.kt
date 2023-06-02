package com.example.domain.usecase

import Resource
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.domain.repo.UserRepo
import retrofit2.Response

class CreatePost(private val userRepo: UserRepo) {

    suspend operator fun invoke(post: Post, result: (Resource<PostModelResponse>) -> Unit) =
        userRepo.createPost(post, result)

}