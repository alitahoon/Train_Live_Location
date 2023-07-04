package com.example.domain.usecase

import Resource
import com.example.domain.entity.UserSignInDataEntity
import com.example.domain.repo.UserRepo

class GetAllUserSignInDataEntity(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        result: (Resource<ArrayList<UserSignInDataEntity>>) -> Unit
    ) = userRepo.getAllUserSignInDataEntity(result)
}