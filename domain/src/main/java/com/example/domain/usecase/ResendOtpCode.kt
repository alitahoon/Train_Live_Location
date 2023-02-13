package com.example.domain.usecase

import android.app.Activity
import com.example.domain.repo.UserRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

class ResendOtpCode(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        phone: String?,
        activity: Activity,
        auth: FirebaseAuth,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ): PhoneAuthOptions = userRepo.resendOtpCode(phone!!,auth,activity, callbacks)
}