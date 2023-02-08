package com.example.domain.usecase

import com.example.domain.repo.UserRepo

class VerifyOtpCode(private val userRepo: UserRepo) {
    suspend operator fun invoke(otbCode:String?)=userRepo.verifyOtpCode(otbCode!!)
}