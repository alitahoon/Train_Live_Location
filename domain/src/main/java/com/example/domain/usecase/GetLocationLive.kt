package com.example.domain.usecase

import androidx.lifecycle.LiveData
import com.example.domain.entity.LocationDetails
import com.example.domain.repo.UserRepo

class GetLocationLive(private val userRepo: UserRepo) {
    suspend operator fun invoke():LiveData<LocationDetails> =userRepo.GetUserLocationLive()
}