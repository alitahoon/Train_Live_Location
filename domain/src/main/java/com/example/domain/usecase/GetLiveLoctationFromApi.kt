package com.example.domain.usecase

import Resource
import com.example.domain.entity.Location_Response
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetLiveLoctationFromApi(private val userRepo: UserRepo) {
    suspend operator fun invoke(trainid: Int,result: (Resource<Location_Response>) -> Unit)=
        userRepo.getLiveLoctationFromApi(trainid, result)
}