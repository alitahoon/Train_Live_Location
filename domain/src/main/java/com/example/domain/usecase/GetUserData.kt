package com.example.domain.usecase

import com.example.domain.entity.userResponse
import com.example.domain.repo.userRepo

class GetUserData(private val UserRepo: userRepo) {
    suspend  operator fun invoke(): userResponse=UserRepo.getUserData()
}