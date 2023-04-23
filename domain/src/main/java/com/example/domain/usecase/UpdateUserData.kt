package com.example.domain.usecase

import Resource
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.UserResponseItem
import com.example.domain.repo.UserRepo

class UpdateUserData(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        userID:Int?,
        userRequest: RegisterUser,
        result: (Resource<String>) -> Unit
    ) = userRepo.updateUserData(userID,userRequest,result)
}