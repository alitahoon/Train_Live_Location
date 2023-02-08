package com.example.trainlivelocation.ui

interface RegisterListener {
    fun onStartRegister()
    fun onSuccessRegister()
    fun onFailure(message:String)
}