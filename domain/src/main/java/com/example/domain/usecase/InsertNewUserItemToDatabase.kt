package com.example.domain.usecase

import Resource
import com.example.domain.entity.UserItemEntity
import com.example.domain.repo.UserRepo

class InsertNewUserItemToDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        userItemEntity: UserItemEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.insertNewUserItemToDatabase(
        userItemEntity,
        result
    )
}