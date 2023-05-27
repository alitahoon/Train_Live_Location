package com.example.domain.usecase

import Resource
import com.example.domain.entity.MessageItemEntity
import com.example.domain.repo.UserRepo

class DeleteStationAlarmFromDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        alarmID: Int,
        result: (Resource<String>) -> Unit
    ) = userRepo.deleteStationAlarmFromDatabase(alarmID,result)
}