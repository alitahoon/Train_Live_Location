package com.example.domain.usecase

import Resource
import com.example.domain.entity.UserResponseItem
import com.example.domain.repo.UserRepo

class GetDataFromSharedPrefrences(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        sharedPrefFile: String?,
        result: (Resource<UserResponseItem>) -> Unit
    ) = userRepo.getDataFromSharedPrefrences(sharedPrefFile,result)
}