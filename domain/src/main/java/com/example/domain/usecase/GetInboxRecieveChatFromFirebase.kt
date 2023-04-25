package com.example.domain.usecase

import Resource
import com.example.domain.entity.Message
import com.example.domain.repo.UserRepo

class GetInboxRecieveChatFromFirebase(private val userRepo: UserRepo) {
    suspend operator fun invoke(phone: String?, result: (Resource<ArrayList<Message>>) -> Unit) =
        userRepo.getInboxRecieveChatFromFirebase(phone,result)
}