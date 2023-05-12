package com.example.domain.usecase

import Resource
import com.example.domain.entity.PushNotification
import com.example.domain.repo.UserRepo

class PushNewTopicNotification(private val userRepo: UserRepo) {
    suspend operator fun invoke(notification: PushNotification, result: (Resource<String>) -> Unit) =
        userRepo.pushNewTopicNotification(notification, result)
}