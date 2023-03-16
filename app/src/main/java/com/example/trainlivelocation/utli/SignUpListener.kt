package com.example.trainlivelocation.utli

interface SignUpListener {
    fun onStartSignUp()
    fun onSuccessSignUp()
    fun onOtbCodeSendToUser()
    fun onVerificationCompleted()
    fun onVerficationSuccess()

    fun nextBtnClicked(type:String)
    fun onFailure(message:String)
}