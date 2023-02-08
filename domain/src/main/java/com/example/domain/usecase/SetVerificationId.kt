package com.example.domain.usecase

import com.example.domain.repo.UserRepo

class SetVerificationId(private val userRepo: UserRepo) {
    suspend operator fun invoke(verification:String?)=userRepo.setVerificationId(verification!!)
}