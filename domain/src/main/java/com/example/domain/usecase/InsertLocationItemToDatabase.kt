package com.example.domain.usecase

import Resource
import com.example.domain.entity.LocationItemEntity
import com.example.domain.repo.UserRepo

class InsertLocationItemToDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        locationItemEntity: LocationItemEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.insertLocationItemToDatabase(
        locationItemEntity,
        result
    )
}