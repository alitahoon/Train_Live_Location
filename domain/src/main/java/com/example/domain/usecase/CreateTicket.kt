package com.example.domain.usecase

import Resource
import com.example.domain.entity.TicketResponseItem
import com.example.domain.repo.UserRepo

class CreateTicket(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        ticketResponseItem: TicketResponseItem,
        result: (Resource<TicketResponseItem>) -> Unit
    ) = userRepo.createTicket(ticketResponseItem,result)
}