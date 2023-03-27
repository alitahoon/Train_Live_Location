package com.example.domain.usecase

import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetUserDataById (private val userRepo: UserRepo) {
    suspend operator fun invoke(userID:Int):Response<userResponseItem> =userRepo.getUserDataById(userID)
}