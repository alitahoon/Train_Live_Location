package com.example.domain.usecase

import Resource
import com.example.domain.entity.LocationItemEntity
import com.example.domain.repo.UserRepo

class UpdateLocationItemFromDatabase(private val userRepo: UserRepo) {

    suspend operator fun invoke(
        locationItemEntity: LocationItemEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.updateLocationItemFromDatabase(locationItemEntity,result)

}