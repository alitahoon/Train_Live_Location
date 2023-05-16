package com.example.domain.usecase

import Resource
import com.example.domain.entity.PushNotification
import com.example.domain.entity.PushPostNotification
import com.example.domain.repo.UserRepo

class PushAddPostNotification(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        notification: PushPostNotification,
        result: (Resource<String>) -> Unit
    ) = userRepo.pushAddPostNotification(notification,result)
}