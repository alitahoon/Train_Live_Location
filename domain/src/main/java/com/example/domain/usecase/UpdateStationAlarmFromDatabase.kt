package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationAlarmEntity
import com.example.domain.repo.UserRepo

class UpdateStationAlarmFromDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        stationAlarmEntity: StationAlarmEntity,
        result: (Resource<String>) -> Unit
    ) = userRepo.updateStationAlarmFromDatabase(stationAlarmEntity,result)
}