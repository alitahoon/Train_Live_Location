package com.example.trainlivelocation.ui

interface SignInListener {
    fun onStartLogin()
    fun onSuccessLogin(userPhone:String,userPassowrd:String)
    fun onSignUpBtnClicked()
    fun onLoginFailure(message:String)
}