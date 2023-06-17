package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationAlarmEntity
import com.example.domain.repo.UserRepo

class GetStationAlarmsFromDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        result: (Resource<ArrayList<StationAlarmEntity>>) -> Unit
    ) = userRepo.getStationAlarmsFromDatabase(result)
}