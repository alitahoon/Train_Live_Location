package com.example.domain.usecase

import Resource
import com.example.domain.entity.NotificationTokenResponse
import com.example.domain.repo.UserRepo

class GetNotificationTokenByUserIDFromApi (private val userRepo: UserRepo){
    suspend operator fun invoke(userId:Int,result: (Resource<NotificationTokenResponse>) -> Unit)=userRepo.getNotificationTokenByUserIDFromApi(userId,result)
}