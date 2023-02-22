package com.example.trainlivelocation.ui

interface SignInListener {
    fun onStartLogin()
    fun onSuccessLogin()
    fun onSignUpBtnClicked()
    fun onSignInBtnClicked()
    fun onLoginFailure(message:String)
}