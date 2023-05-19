package com.example.domain.usecase

import Resource
import com.example.domain.entity.NotificationTokenResponse
import com.example.domain.entity.NotificationTokenResponseInTrain
import com.example.domain.repo.UserRepo

class GetNotificationTokenForUsersInTrain(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        trainId: Int,
        result: (Resource<ArrayList<NotificationTokenResponseInTrain>>) -> Unit
    ) = userRepo.getNotificationTokenForUsersInTrain(trainId,result)
}