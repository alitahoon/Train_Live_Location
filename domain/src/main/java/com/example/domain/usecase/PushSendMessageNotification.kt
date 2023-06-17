package com.example.domain.usecase

import Resource
import com.example.domain.entity.PushMessageNotification
import com.example.domain.repo.UserRepo

class PushSendMessageNotification(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        notification: PushMessageNotification,
        result: (Resource<String>) -> Unit
    ) = userRepo.pushSendMessageNotification(notification,result)

}