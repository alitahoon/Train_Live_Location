package com.example.domain.usecase

import com.example.domain.entity.Location_Response
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetLiveLoctationFromApi(private val userRepo: UserRepo) {
    suspend operator fun invoke(trainid: Int): Response<Location_Response> =
        userRepo.getLiveLoctationFromApi(trainid)
}