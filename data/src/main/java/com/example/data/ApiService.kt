package com.example.data

import retrofit2.http.GET

interface ApiService {
    @GET("")
    fun getUserData(): user


}