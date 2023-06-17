package com.example.domain.usecase

import Resource
import com.example.domain.entity.TrainItemEntity
import com.example.domain.repo.UserRepo

class InsertNewTrainItemToDatabase(private  val userRepo: UserRepo) {
    suspend operator fun invoke(
        trainItemEntity: TrainItemEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.insertNewTrainItemToDatabase(
        trainItemEntity,
        result
    )
}