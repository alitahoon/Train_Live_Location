package com.example.domain.usecase

import Resource
import android.app.Activity
import android.location.Location
import com.example.domain.entity.LocationDetails
import com.example.domain.repo.UserRepo

class GetUserLocation (private val userRepo: UserRepo){
    suspend operator fun invoke(result: (Resource<LocationDetails>) -> Unit)=userRepo.getUserLocation(result)
}