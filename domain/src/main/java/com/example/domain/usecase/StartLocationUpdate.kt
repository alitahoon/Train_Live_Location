package com.example.domain.usecase

import com.example.domain.repo.UserRepo

class StartLocationUpdate(private val userRepo: UserRepo) {
    suspend operator fun invoke(interval:Long?)=userRepo.startLocationUpdate(interval)
}