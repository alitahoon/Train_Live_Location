package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationItemEntity
import com.example.domain.repo.UserRepo

class InsertNewStationToDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        stationItemEntity: StationItemEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.insertnewStationToDatabase(stationItemEntity,result)
}