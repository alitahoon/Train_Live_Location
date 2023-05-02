package com.example.domain.usecase

import Resource
import androidx.lifecycle.LifecycleService
import com.example.domain.repo.UserRepo

class GetTrainLocationInForgroundService(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        trainId: Int?,
        result: (Resource<LifecycleService>) -> Unit
    ) = userRepo.getTrainLocationInForgroundService(trainId,result)
}