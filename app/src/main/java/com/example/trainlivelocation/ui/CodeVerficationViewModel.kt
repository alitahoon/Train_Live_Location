package com.example.trainlivelocation.ui

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CodeVerficationViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
    private val isUserVerified: IsUserVerified,
    private val resendOtpCode: ResendOtpCode,
    private val sendOtpToPhone: SendOtpToPhone,
    private val setVerificationId: SetVerificationId,
    private val verifyOtpCode: VerifyOtpCode,
):ViewModel (){
    var codeVerfication:String?=null
}