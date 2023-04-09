package com.example.domain.usecase

import Resource
import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetUserData(private val UserRepo: UserRepo) {
    suspend operator fun invoke(
        userPhone: String?,
        userPassword: String?,
        result: (Resource<userResponseItem>) -> Unit
    ) = UserRepo.getUserData(userPhone, userPassword, result)
}