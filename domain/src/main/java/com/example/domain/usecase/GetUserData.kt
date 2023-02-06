package com.example.domain.usecase

import com.example.domain.entity.user
import com.example.domain.repo.userRepo

class GetUserData(private val UserRepo: userRepo) {
    operator fun invoke(): user=UserRepo.getUserData()
}