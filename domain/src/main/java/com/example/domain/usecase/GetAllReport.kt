package com.example.domain.usecase

import Resource
import com.example.domain.entity.ReportPostResponse
import com.example.domain.repo.UserRepo

class GetAllReport(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        result: (Resource<ArrayList<ReportPostResponse>>) -> Unit
    ) = userRepo.getAllReport(result)
}