package com.example.domain.usecase

import Resource
import com.example.domain.entity.RouteDirctionEntity
import com.example.domain.repo.UserRepo

class InsertnewDirctionRouteInDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(routeDirctionEntity: RouteDirctionEntity,
                                result: (Resource<String>) -> Unit)=userRepo.insertnewDirctionRouteInDatabase(routeDirctionEntity,result)
}