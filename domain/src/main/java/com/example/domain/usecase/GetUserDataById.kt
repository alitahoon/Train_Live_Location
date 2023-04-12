package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Response

class GetUserDataById(private val userRepo: UserRepo) {
    suspend operator fun invoke(userID: Int, result: (Resource<userResponseItem>) -> Unit) =
        userRepo.getUserDataById(userID, result)
}