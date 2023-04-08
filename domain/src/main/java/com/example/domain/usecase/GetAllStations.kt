package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationResponseItem
import com.example.domain.repo.UserRepo

class GetAllStations(private val userRepo: UserRepo) {
    suspend operator fun invoke(result:(Resource<ArrayList<StationResponseItem>>)->Unit)=userRepo.getAllStations(result)
}