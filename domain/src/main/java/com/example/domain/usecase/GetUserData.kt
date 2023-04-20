package com.example.domain.usecase

import Resource
import com.example.domain.entity.UserResponseItem
import com.example.domain.repo.UserRepo

class GetUserData(private val UserRepo: UserRepo) {
    suspend operator fun invoke(
        userPhone: String?,
        userPassword: String?,
        result: (Resource<UserResponseItem>) -> Unit
    ) = UserRepo.getUserData(userPhone, userPassword, result)
}