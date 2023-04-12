package com.example.domain.usecase

import Resource
import com.example.domain.entity.TrainResponseItem
import com.example.domain.repo.UserRepo

class GetAllTrains(private val userRepo: UserRepo) {
    suspend operator fun invoke(result:(Resource<ArrayList<TrainResponseItem>>)->Unit)=userRepo.getAllTrains(result)
}