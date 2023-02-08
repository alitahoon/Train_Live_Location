package com.example.domain.usecase

import com.example.domain.repo.UserRepo

class IsUserVerified(private val userRepo: UserRepo) {
    suspend operator fun invoke():Boolean=userRepo.isUserVerified()
}