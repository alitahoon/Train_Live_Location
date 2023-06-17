package com.example.domain.usecase

import Resource
import com.example.domain.entity.NewsItemEntity
import com.example.domain.repo.UserRepo

class InsertNewsItemToDatabase(private val userRepo: UserRepo) {

    suspend operator fun invoke(
        newsItemEntity: NewsItemEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.insertNewsItemToDatabase(
        newsItemEntity,
        result
    )

}