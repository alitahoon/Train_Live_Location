package com.example.domain.usecase

import Resource
import com.example.domain.entity.NotificatonToken
import com.example.domain.repo.UserRepo

class SendUserNotificationTokenToFirebase(private val userRepo: UserRepo) {
    suspend operator fun invoke(token: NotificatonToken?, result: (Resource<String?>) -> Unit) =
        userRepo.sendUserNotificationTokenToFirebase(token,result)
}