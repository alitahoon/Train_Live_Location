package com.example.domain.usecase

import Resource
import com.example.domain.repo.UserRepo

class ClearStationsFromDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        result: (Resource<String>) -> Unit
    ) = userRepo.clearStationsFromDatabase(result)
}