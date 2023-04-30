package com.example.domain.usecase

import Resource
import com.example.domain.entity.DoctorResponseItem
import com.example.domain.repo.UserRepo

class GetDoctorInTrain(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        trainId:Int,
        result: (Resource<ArrayList<DoctorResponseItem>>) -> Unit
    ) = userRepo.getDoctorInTrain(trainId,result)
}