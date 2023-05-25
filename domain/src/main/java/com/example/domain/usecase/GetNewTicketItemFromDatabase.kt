package com.example.domain.usecase

import Resource
import com.example.domain.entity.TicketItemEntity
import com.example.domain.repo.UserRepo

class GetNewTicketItemFromDatabase(private val userRepo: UserRepo) {

    suspend operator fun invoke(result: (Resource<ArrayList<TicketItemEntity>>) -> Unit) =
        userRepo.getNewTicketItemFromDatabase (result)

}