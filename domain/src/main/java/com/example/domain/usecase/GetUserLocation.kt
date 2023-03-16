package com.example.domain.usecase

import android.app.Activity
import android.location.Location
import com.example.domain.entity.LocationDetails
import com.example.domain.repo.UserRepo

class GetUserLocation (private val userRepo: UserRepo){
    suspend operator fun invoke(callback :(LocationDetails)->Unit)=userRepo.getUserLocation(callback)
}