package com.example.domain.usecase

import Resource
import com.example.domain.entity.RouteDirctionEntity
import com.example.domain.repo.UserRepo

class GetDirctionRoutesFromDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        result: (Resource<ArrayList<RouteDirctionEntity>>) -> Unit
    ) = userRepo.getDirctionRoutesFromDatabase(result)
}