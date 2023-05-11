package com.example.domain.usecase

import com.example.domain.entity.CreateNewsResponseItem
import com.example.domain.entity.GetNewsResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Response

class CreateNews(private val userRepo: UserRepo) {
    suspend operator fun invoke(result: (Response<CreateNewsResponseItem>) -> Unit) =
        userRepo.createNews(result)
}