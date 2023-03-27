package com.example.domain.usecase

import androidx.appcompat.app.AppCompatActivity
import com.example.domain.repo.UserRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

class ResendOtpCode(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        phone: String?,
        callback: (result: String?) -> Unit
    ) = userRepo.resendOtpCode(phone!!,callback)
}