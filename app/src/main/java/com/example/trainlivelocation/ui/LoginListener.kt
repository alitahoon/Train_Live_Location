package com.example.trainlivelocation.ui

interface LoginListener {
    fun onStartLogin()
    fun onSuccessLogin()
    fun onLoginFailure(message:String)
}