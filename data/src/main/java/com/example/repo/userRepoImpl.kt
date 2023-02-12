package com.example.repo

import android.app.Activity
import com.example.data.ApiService
import com.example.data.FirebaseService
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.example.domain.repo.UserRepo
import retrofit2.Call
import retrofit2.Response

class userRepoImpl(private val apiService:ApiService,private val firebaseService: FirebaseService): UserRepo{

    override  suspend fun getUserData(userPhone:String?,userPassword:String?): Response<ArrayList<userResponseItem>> =apiService.getUserData(userPhone!!,userPassword!!)
    override  suspend fun addNewUser(user: RegisterUser?):Response<ArrayList<userResponseItem>> =apiService.addNewUser(user)
    override suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity)=firebaseService.sendOtpToPhone(phoneNumber,activity)

    override suspend fun resendOtpCode(phoneNumber: String, activity: Activity) =firebaseService.resendOtpCode(phoneNumber,activity)


}