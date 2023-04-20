package com.example.domain.usecase

import Resource
import com.example.domain.entity.UserResponseItem
import com.example.domain.repo.UserRepo

class GetUserDataById(private val userRepo: UserRepo) {
    suspend operator fun invoke(userID: Int, result: (Resource<UserResponseItem>) -> Unit) =
        userRepo.getUserDataById(userID, result)
}