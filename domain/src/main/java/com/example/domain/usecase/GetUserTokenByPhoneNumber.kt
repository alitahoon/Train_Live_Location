package com.example.domain.usecase

import com.example.domain.repo.UserRepo
import Resource

class GetUserTokenByPhoneNumber(private val userRepo: UserRepo) {
    suspend operator fun invoke (
        phoneNumber: String,
        result: (Resource<String>) -> Unit
    ) = userRepo.getUserTokenByPhoneNumber(phoneNumber,result)
}