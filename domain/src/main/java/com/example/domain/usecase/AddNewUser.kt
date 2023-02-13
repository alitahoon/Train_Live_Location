package com.example.domain.usecase

import com.example.domain.entity.RegisterUser
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Call
import retrofit2.Response

class AddNewUser (private val UserRepo: UserRepo){
    suspend operator fun invoke(user: RegisterUser?): Response<ArrayList<userResponseItem>> =UserRepo.addNewUser(user)
}