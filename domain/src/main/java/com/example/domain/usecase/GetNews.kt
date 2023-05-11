package com.example.domain.usecase

import com.example.domain.entity.GetNewsResponseItem
import com.example.domain.entity.StationResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetNews(private val userRepo: UserRepo) {
    suspend operator fun invoke(result: (Response<ArrayList<GetNewsResponseItem>>) -> Unit) =
        userRepo.getNews(result)
}