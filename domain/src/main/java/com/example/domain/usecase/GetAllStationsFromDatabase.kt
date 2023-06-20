package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationItemEntity
import com.example.domain.repo.UserRepo

class GetAllStationsFromDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        result: (Resource<ArrayList<StationItemEntity>>) -> Unit
    ) = userRepo.getAllStationsFromDatabase(result)
}