package com.example.domain.usecase

import androidx.lifecycle.LifecycleService
import com.example.domain.repo.UserRepo

class GetLocationTrackForegroundService (private val userRepo: UserRepo){
    suspend operator fun invoke(trainId:Int?): LifecycleService =userRepo.getLocationTrackForegroundService(trainId!!)

}