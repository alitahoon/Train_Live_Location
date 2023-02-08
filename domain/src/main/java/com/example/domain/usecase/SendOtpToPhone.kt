package com.example.domain.usecase

import android.app.Activity
import com.example.domain.repo.UserRepo

class SendOtpToPhone (private val userRepo: UserRepo,private val activity: Activity){
    suspend operator fun invoke(phone:String?)=userRepo.sendOtpToPhone(phone!!, activity)
}