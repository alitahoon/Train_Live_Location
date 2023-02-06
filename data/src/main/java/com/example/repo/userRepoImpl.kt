package com.example.repo

import com.example.data.ApiService
import com.example.data.FirebaseService
import com.example.domain.entity.user
import com.example.domain.repo.userRepo

class userRepoImpl(private val apiService:ApiService,private val firebaseService: FirebaseService): userRepo{
    override fun checkUserFirebaseAuth(): Boolean =firebaseService.checkUserAuth()

    override fun getUserData(): user =apiService.getUserData()
}