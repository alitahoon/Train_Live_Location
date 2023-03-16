package com.example.domain.usecase

import com.example.domain.repo.UserRepo

class StopLocationUpdate (private val userRepo: UserRepo){
    suspend operator fun invoke()=userRepo.stopLocationUpdate()
}