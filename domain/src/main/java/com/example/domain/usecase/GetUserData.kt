package com.example.domain.usecase

import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Call
import retrofit2.Response

class GetUserData(private val UserRepo: UserRepo) {
    suspend  operator fun invoke(userPhone:String?,userPassword:String?): Response<ArrayList<userResponseItem>> =UserRepo.getUserData(userPhone,userPassword)
}