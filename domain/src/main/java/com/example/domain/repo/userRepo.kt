package com.example.domain.repo

import com.example.domain.entity.user

interface userRepo {
    fun checkUserFirebaseAuth():Boolean
    fun getUserData(): user
}