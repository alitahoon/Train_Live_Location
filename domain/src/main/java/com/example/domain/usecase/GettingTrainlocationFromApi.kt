package com.example.domain.usecase

import Resource
import com.example.domain.entity.Location_Response
import com.example.domain.repo.UserRepo

class GettingTrainlocationFromApi(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        trainId: Int, result: (Resource<Location_Response>) -> Unit
    ) = userRepo.gettingTrainlocationFromApi(trainId,result)
}