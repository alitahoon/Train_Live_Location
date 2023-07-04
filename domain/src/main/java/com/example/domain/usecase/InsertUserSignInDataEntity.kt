package com.example.domain.usecase

import Resource
import com.example.domain.entity.UserSignInDataEntity
import com.example.domain.repo.UserRepo

class InsertUserSignInDataEntity(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        userSignInDataEntity: UserSignInDataEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.insertUserSignInDataEntity(
        userSignInDataEntity,
        result
    )
}