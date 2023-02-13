package com.example.trainlivelocation.ui

import com.example.domain.entity.userResponse
import kotlinx.coroutines.flow.StateFlow

interface LoginListener {
    fun onStartLogin()
    fun onSuccessLogin()
    fun onLoginFailure(message:String)
}