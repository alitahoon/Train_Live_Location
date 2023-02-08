package com.example.data

import android.app.Activity

interface FirebaseService {
    fun checkUserAuthWithPhone():Boolean
    suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity)

    suspend fun resendOtpCode(phoneNumber: String, activity: Activity)

    suspend fun isUserVerified(): Boolean

    suspend fun setVerificationId(verificationId: String)

    fun verifyOtpCode(otpCode: String)
}