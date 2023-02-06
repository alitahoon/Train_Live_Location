package com.example.domain.usecase

import com.example.domain.repo.userRepo

class CheckUserAuthFB (private val UserRepo: userRepo){
    suspend operator fun invoke()=UserRepo.checkUserFirebaseAuth()
}