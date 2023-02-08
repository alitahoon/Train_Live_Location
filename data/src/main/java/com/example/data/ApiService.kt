package com.example.data

import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("")
    fun getUserData(): userResponseItem

    @POST("")
    fun addUSerData():userResponseItem


}