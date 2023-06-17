package com.example.domain.usecase

import Resource
import com.example.domain.entity.TrainItemEntity
import com.example.domain.entity.UserItemEntity
import com.example.domain.repo.UserRepo

class GetNewTrainItemFromDatabase(private val userRepo: UserRepo) {

    suspend operator fun invoke(result: (Resource<ArrayList<TrainItemEntity>>) -> Unit) =
        userRepo.getNewTrainItemFromDatabase(result)

}