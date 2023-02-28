package com.example.data

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential

interface FirebaseServiceCallbacks {
    fun onOtbResend()
    fun onOtbResendFailure(error:Exception?)
    fun onImageSendSuccessfully()
    fun onImageSendFailure(error:Exception?)
}