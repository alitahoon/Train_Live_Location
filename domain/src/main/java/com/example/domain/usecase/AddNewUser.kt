package com.example.domain.usecase

import Resource
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.UserResponseItem
import com.example.domain.repo.UserRepo

class AddNewUser (private val UserRepo: UserRepo){
    suspend operator fun invoke(user: RegisterUser?,result:(Resource<UserResponseItem>)->Unit) =UserRepo.addNewUser(user,result)
}