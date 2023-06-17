package com.example.domain.usecase

import Resource
import com.example.domain.entity.MessageItemEntity
import com.example.domain.repo.UserRepo

class GetNewMessageItemFromDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(result: (Resource<ArrayList<MessageItemEntity>>) -> Unit) =
        userRepo.getNewMessageItemFromDatabase (result)
}