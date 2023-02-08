package com.example.domain.usecase

import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo

class AddNewUser (private val UserRepo: UserRepo){
    suspend operator fun invoke():userResponseItem=UserRepo.addNewUser()
}