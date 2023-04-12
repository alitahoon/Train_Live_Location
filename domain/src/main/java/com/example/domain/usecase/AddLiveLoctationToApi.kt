package com.example.domain.usecase

import Resource
import com.example.domain.entity.Location_Request
import com.example.domain.entity.Location_Request_with_id
import com.example.domain.repo.UserRepo
import retrofit2.Response

class AddLiveLoctationToApi(private val userRepo: UserRepo) {
    suspend operator fun invoke(locationRequest: Location_Request,result: (Resource<Location_Request_with_id>) -> Unit) =
        userRepo.addLiveLoctationToApi(locationRequest,result)
}