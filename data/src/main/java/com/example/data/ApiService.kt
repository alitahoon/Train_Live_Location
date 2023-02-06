package com.example.data

import com.example.domain.entity.user
import com.example.domain.repo.userRepo
import retrofit2.http.GET

interface ApiService {
    @GET("")
    fun getUserData(): user


}