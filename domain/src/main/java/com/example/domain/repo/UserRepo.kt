package com.example.domain.repo

import android.app.Activity
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem

interface UserRepo {
    fun getUserData(): userResponseItem

    fun addNewUser():userResponseItem
    suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity)

    suspend fun resendOtpCode(phoneNumber: String, activity: Activity)

    suspend fun isUserVerified(): Boolean

    suspend fun setVerificationId(verificationId: String)

    fun verifyOtpCode(otpCode: String)
}