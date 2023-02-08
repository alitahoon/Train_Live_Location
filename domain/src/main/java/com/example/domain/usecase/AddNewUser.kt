package com.example.domain.usecase

import com.example.domain.repo.UserRepo

class AddNewUser (private val UserRepo: UserRepo){
    suspend operator fun invoke()=UserRepo.addNewUser()
}