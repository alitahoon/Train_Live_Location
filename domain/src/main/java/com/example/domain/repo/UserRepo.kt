package com.example.domain.repo

import android.app.Activity
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import retrofit2.Call
import retrofit2.Response

interface UserRepo {
     suspend fun getUserData(userPhone:String?,userPassword:String?): Response<ArrayList<userResponseItem>>

     suspend fun  addNewUser(user: RegisterUser?):Response<ArrayList<userResponseItem>>
    suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity)
    suspend fun resendOtpCode(phoneNumber: String, activity: Activity)


}