package com.example.domain.usecase

import Resource
import com.example.domain.entity.MessageItemEntity
import com.example.domain.repo.UserRepo

class InsertNewMessageItemToDatabase(private  val userRepo: UserRepo) {
    suspend operator fun invoke(
        messageItemEntity: MessageItemEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.insertNewMessageItemToDatabase(
        messageItemEntity,
        result
    )
}