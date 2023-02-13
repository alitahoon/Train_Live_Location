package com.example.repo

import android.app.Activity
import com.example.data.ApiService
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit

class userRepoImpl(private val apiService: ApiService) : UserRepo {

    override suspend fun getUserData(
        userPhone: String?,
        userPassword: String?
    ): Response<ArrayList<userResponseItem>> = apiService.getUserData(userPhone!!, userPassword!!)

    override suspend fun addNewUser(user: RegisterUser?): Response<ArrayList<userResponseItem>> =
        apiService.addNewUser(user)

    override suspend fun sendOtpToPhone(
        phoneNumber: String,
        auth: FirebaseAuth,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
    ): PhoneAuthOptions {
        auth.setLanguageCode("ar")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        return options
    }

    override suspend fun resendOtpCode(
        phoneNumber: String,
        auth: FirebaseAuth,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ): PhoneAuthOptions {
        auth.setLanguageCode("ar")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        return options
    }

    override suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        auth: FirebaseAuth
    ): Task<AuthResult> {
        return auth.signInWithCredential(credential)
    }


}