package com.example.domain.usecase

import Resource
import com.example.domain.entity.NewsItemEntity
import com.example.domain.repo.UserRepo

class GetNewsItemFromDatabase(private val userRepo: UserRepo) {

    suspend operator fun invoke(result: (Resource<ArrayList<NewsItemEntity>>) -> Unit) =
        userRepo.getNewsItemFromDatabase(result)

}