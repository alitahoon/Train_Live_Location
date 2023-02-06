package com.example.domain.repo

import com.example.domain.entity.userResponse

interface userRepo {
    fun checkUserFirebaseAuth():Boolean
    fun getUserData(): userResponse

    fun addNewUser()
}