package com.example.data

import com.example.domain.entity.RegisterUser
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/user/UserLogin")
    suspend fun getUserData(
        @Query("phone") userPhone: String, @Query("password") userPassword: String
    ): Response<ArrayList<userResponseItem>>

    @POST("api/user/CreateUser")
    suspend fun addNewUser(@Body user: RegisterUser?): Response<ArrayList<userResponseItem>>




}