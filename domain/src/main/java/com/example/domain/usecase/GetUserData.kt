package com.example.domain.usecase

import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo

class GetUserData(private val UserRepo: UserRepo) {
    suspend  operator fun invoke(): userResponseItem=UserRepo.getUserData()
}