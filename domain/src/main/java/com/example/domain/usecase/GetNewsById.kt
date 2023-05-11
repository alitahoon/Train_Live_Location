package com.example.domain.usecase

import com.example.domain.entity.GetNewsByIdResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetNewsById(private val userRepo: UserRepo) {
    suspend operator fun invoke(newsId: Int?, result: (Response<GetNewsByIdResponseItem>) -> Unit) =
        userRepo.getStationById(newsId,result)
}