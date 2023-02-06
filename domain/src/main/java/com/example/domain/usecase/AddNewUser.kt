package com.example.domain.usecase

import com.example.domain.repo.userRepo

class AddNewUser (private val UserRepo: userRepo){
    suspend operator fun invoke()=UserRepo.addNewUser()
}