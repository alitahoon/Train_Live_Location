package com.example.domain.usecase

import Resource
import com.example.domain.repo.UserRepo
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsResult

class GetLocationDirctionFromOpenRouteService(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        origin: LatLng,
        destination: LatLng,
        result: (Resource<DirectionsResult>) -> Unit
    ) = userRepo.getLocationDirctionFromOpenRouteService(origin,destination,result)
}