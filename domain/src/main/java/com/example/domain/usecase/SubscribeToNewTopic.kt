package com.example.domain.usecase

import Resource
import com.example.domain.repo.UserRepo

class SubscribeToNewTopic(private val userRepo: UserRepo) {
    suspend operator fun invoke(topicInput: String,result: (Resource<String>) -> Unit)=userRepo.subscribeToNewTopic(topicInput,result)
}