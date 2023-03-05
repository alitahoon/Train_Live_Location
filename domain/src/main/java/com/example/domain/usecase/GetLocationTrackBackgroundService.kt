package com.example.domain.usecase

import android.app.Service
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import com.example.domain.repo.UserRepo

class GetLocationTrackBackgroundService(private val userRepo: UserRepo) {
    suspend operator fun invoke(trainid: Int,userid:Int): LifecycleService =userRepo.getLocationTrackBackgroundService(trainid,userid)
}