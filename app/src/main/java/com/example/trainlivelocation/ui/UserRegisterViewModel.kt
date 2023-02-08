package com.example.trainlivelocation.ui

import com.example.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
    private val isUserVerified: IsUserVerified,
    private val resendOtpCode: ResendOtpCode,
    private val sendOtpToPhone: SendOtpToPhone,
    private val setVerificationId: SetVerificationId,
    private val verifyOtpCode: VerifyOtpCode,
){


}