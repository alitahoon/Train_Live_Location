package com.example.domain.usecase

import Resource
import com.example.domain.entity.Message
import com.example.domain.repo.UserRepo

class GetChatFromFirebase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        senderPhone: String?,
        recieverPhone: String?,
        result: (Resource<ArrayList<Message>>) -> Unit
    ) = userRepo.getChatFromFirebase(senderPhone,recieverPhone,result)
}