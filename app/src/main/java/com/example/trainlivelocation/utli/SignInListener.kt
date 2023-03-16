package com.example.trainlivelocation.utli

interface SignInListener {
    fun onStartLogin()
    fun onSuccessLogin(userPhone:String,userPassowrd:String)
    fun onSignUpBtnClicked()
    fun onLoginFailure(message:String)
}