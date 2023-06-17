package com.example.domain.usecase

import Resource
import com.example.domain.entity.ReportResponseItem
import com.example.domain.repo.UserRepo

class CreateReport(private val userRepo: UserRepo) {

    suspend operator fun invoke(result: (Resource<ReportResponseItem>) -> Unit) =
        userRepo.createReport(result)

}