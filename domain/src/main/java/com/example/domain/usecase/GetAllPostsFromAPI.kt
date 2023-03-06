package com.example.domain.usecase

import com.example.domain.entity.Post
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetAllPostsFromAPI(private val userRepo: UserRepo) {
    suspend operator fun invoke():Response<ArrayList<Post>> = userRepo.getAllPostsFromAPI()
}