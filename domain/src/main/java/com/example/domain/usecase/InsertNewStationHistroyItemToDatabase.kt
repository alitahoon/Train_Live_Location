package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationHistoryAlarmEntity
import com.example.domain.repo.UserRepo

class InsertNewStationHistroyItemToDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        stationHistoryAlarmEntity: StationHistoryAlarmEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.insertNewStationHistroyItemToDatabase(stationHistoryAlarmEntity,result)
}