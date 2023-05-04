package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationUsersNotificationResponseItem
import com.example.domain.entity.UserInTrainResponseItem
import com.example.domain.repo.UserRepo

class GetUserInTrain(private val userRepo: UserRepo) {
    suspend operator fun invoke (
        trainId: Int,
        result: (Resource<ArrayList<UserInTrainResponseItem>>) -> Unit
    ) = userRepo.getUserInTrain(trainId,result)
}