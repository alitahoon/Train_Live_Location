package com.example.domain.usecase

import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetUserData(private val UserRepo: UserRepo) {
    suspend  operator fun invoke(userPhone:String?,userPassword:String?): Response<userResponseItem> =UserRepo.getUserData(userPhone,userPassword)
}