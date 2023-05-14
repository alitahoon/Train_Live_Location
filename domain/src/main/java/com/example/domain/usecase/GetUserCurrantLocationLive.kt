package com.example.domain.usecase

import Resource
import android.location.Location
import com.example.domain.repo.UserRepo

class GetUserCurrantLocationLive(private val userRepo: UserRepo) {
    suspend operator fun invoke(result: (Resource<Location>) -> Unit)=userRepo.getUserCurrantLocationLive(result)
}