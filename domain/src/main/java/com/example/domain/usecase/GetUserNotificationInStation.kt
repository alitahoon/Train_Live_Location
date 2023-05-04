package com.example.domain.usecase

import Resource
import com.example.domain.entity.StationUsersNotificationResponseItem
import com.example.domain.repo.UserRepo

class GetUserNotificationInStation(private val userRepo: UserRepo) {
    suspend operator fun invoke (
        stationId: Int,
        result: (Resource<ArrayList<StationUsersNotificationResponseItem>>) -> Unit
    ) = userRepo.getUserNotificationInStation(stationId,result)
}