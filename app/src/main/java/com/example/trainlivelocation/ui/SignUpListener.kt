package com.example.trainlivelocation.ui

interface SignUpListener {
    fun onStartSignUp()
    fun onSuccessSignUp()
    fun onOtbCodeSendToUser()
    fun onVerificationCompleted()
    fun onVerficationSuccess()

    fun nextBtnClicked()
    fun onFailure(message:String)
}