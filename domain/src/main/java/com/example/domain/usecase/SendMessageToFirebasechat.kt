package com.example.domain.usecase

import Resource
import com.example.domain.repo.UserRepo

class SendMessageToFirebasechat(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        message: String?,
        senderPhone: String?,
        reciverPhone: String?,
        result: (Resource<String>) -> Unit
    ) = userRepo.sendMessageToFirebasechat(message,senderPhone,reciverPhone,result)
}