package com.example.domain.usecase

import com.example.domain.repo.UserRepo
import com.google.firebase.auth.PhoneAuthCredential

class CreateAPhoneAuthCredential(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        code: String?,
        callback: (result: PhoneAuthCredential?) -> Unit
    ) =
        userRepo.createAPhoneAuthCredential(code,callback)
}