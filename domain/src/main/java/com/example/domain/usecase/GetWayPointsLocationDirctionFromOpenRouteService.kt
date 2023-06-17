package com.example.domain.usecase

import Resource
import com.example.domain.entity.OpenRouteDirectionResult
import com.example.domain.repo.UserRepo
import com.google.android.gms.maps.model.LatLng

class GetWayPointsLocationDirctionFromOpenRouteService(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        wayPoints: List<LatLng>,
        result: (Resource<OpenRouteDirectionResult>) -> Unit
    ) = userRepo.getWayPointsLocationDirctionFromOpenRouteService(wayPoints, result)
}