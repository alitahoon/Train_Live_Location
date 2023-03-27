package com.example.domain.usecase

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.example.domain.repo.UserRepo

class SetFirebaseServiceActivity(private val userRepo: UserRepo) {
    suspend operator fun invoke(activity: AppCompatActivity)=userRepo.setFirebaseServiceActivity(activity)
}