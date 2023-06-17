package com.example.domain.usecase

import Resource
import com.example.domain.entity.TicketItemEntity
import com.example.domain.repo.UserRepo

class InsertNewTicketItemToDatabase(private  val userRepo: UserRepo) {

    suspend operator fun invoke(
        ticketItemEntity: TicketItemEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.insertNewTicketItemToDatabase(
        ticketItemEntity,
        result
    )

}