package com.example.domain.usecase

import Resource
import com.example.domain.entity.stationResponse
import com.example.domain.repo.UserRepo

class GetAllStations(private val userRepo: UserRepo) {
    suspend operator fun invoke(result:(Resource<stationResponse>)->Unit)=userRepo.getAllStations(result)
}