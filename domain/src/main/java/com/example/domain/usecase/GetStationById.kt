package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationResponseItem
import com.example.domain.repo.UserRepo

class GetStationById(private val userRepo: UserRepo) {
    suspend operator fun invoke(stationID: Int?, result: (Resource<StationResponseItem>) -> Unit) =
        userRepo.getStationById(stationID,result)
}