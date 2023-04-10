package com.example.domain.usecase

import Resource
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetAllPostsFromAPI(private val userRepo: UserRepo) {
    suspend operator fun invoke(result: (Resource<ArrayList<PostModelResponse>>)->Unit) = userRepo.getAllPostsFromAPI(result)
}