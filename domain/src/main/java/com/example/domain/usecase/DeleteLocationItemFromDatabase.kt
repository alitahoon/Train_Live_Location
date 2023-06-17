package com.example.domain.usecase

import Resource
import com.example.domain.repo.UserRepo

class DeleteLocationItemFromDatabase(private val userRepo: UserRepo){

    suspend operator fun invoke(
        locationID: Long,
        result: (Resource<String>) -> Unit
    ) = userRepo.deleteLocationItemFromDatabase(locationID,result)

}