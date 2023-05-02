package com.example.domain.usecase

import Resource
import android.app.Service
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import com.example.domain.repo.UserRepo

class GetLocationTrackBackgroundService(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        trainid: Int, userid: Int, result: (Resource<LifecycleService>) -> Unit
    ) =
        userRepo.getLocationTrackBackgroundService(trainid, userid,result)
}