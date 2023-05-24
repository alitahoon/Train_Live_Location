package com.example.domain.usecase

import Resource
import com.example.domain.entity.UserItemEntity
import com.example.domain.repo.UserRepo

class GetNewUserItemFromDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(result: (Resource<ArrayList<UserItemEntity>>) -> Unit) =
        userRepo.getNewUserItemFromDatabase(result)
}