package com.example.repo

import android.app.Activity
import com.example.data.ApiService
import com.example.data.FirebaseService
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo

class userRepoImpl(private val apiService:ApiService,private val firebaseService: FirebaseService): UserRepo{

    override fun getUserData(): userResponseItem =apiService.getUserData()
    override fun addNewUser():userResponseItem=apiService.addUSerData()

    override suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity) =firebaseService.sendOtpToPhone(phoneNumber!!,activity)

    override suspend fun resendOtpCode(phoneNumber: String, activity: Activity) =firebaseService.resendOtpCode(phoneNumber!!,activity)

    override suspend fun isUserVerified(): Boolean =firebaseService.isUserVerified()

    override suspend fun setVerificationId(verificationId: String) =firebaseService.setVerificationId(verificationId)

    override fun verifyOtpCode(otpCode: String)=firebaseService.verifyOtpCode(otpCode)

}