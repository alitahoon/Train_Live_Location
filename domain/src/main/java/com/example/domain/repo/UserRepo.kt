package com.example.domain.repo

import android.app.Activity
import com.example.domain.entity.userResponse

interface UserRepo {
    fun getUserData(): userResponse

    fun addNewUser()
    suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity)

    suspend fun resendOtpCode(phoneNumber: String, activity: Activity)

    suspend fun isUserVerified(): Boolean

    suspend fun setVerificationId(verificationId: String)

    fun verifyOtpCode(otpCode: String)
}