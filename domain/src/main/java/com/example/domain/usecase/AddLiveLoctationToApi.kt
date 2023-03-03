package com.example.domain.usecase

import com.example.domain.entity.Location_Request
import com.example.domain.entity.Location_Request_with_id
import com.example.domain.repo.UserRepo
import retrofit2.Response

class AddLiveLoctationToApi(private val userRepo: UserRepo) {
    suspend operator fun invoke(locationRequest: Location_Request): Response<Location_Request_with_id> =
        userRepo.addLiveLoctationToApi(locationRequest)
}