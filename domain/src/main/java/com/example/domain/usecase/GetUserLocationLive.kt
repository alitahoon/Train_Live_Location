package com.example.domain.usecase

import Resource
import androidx.lifecycle.LiveData
import com.example.domain.entity.LocationDetails
import com.example.domain.repo.UserRepo

class GetUserLocationLive(private val userRepo: UserRepo) {
    suspend operator fun invoke(result:(LiveData<LocationDetails>)->Unit) =userRepo.GetUserLocationLive(result)
}