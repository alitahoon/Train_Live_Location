package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationHistoryAlarmEntity
import com.example.domain.repo.UserRepo

class GetStationHistroyItemsFromDatabase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        result: (Resource<ArrayList<StationHistoryAlarmEntity>>) -> Unit
    ) = userRepo.getStationHistroyItemsFromDatabase(result)
}