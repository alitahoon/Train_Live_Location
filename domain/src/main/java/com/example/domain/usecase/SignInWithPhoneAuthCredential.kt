package com.example.domain.usecase

import com.example.domain.repo.UserRepo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential

class SignInWithPhoneAuthCredential(private var userRepo: UserRepo) {
    suspend operator fun invoke( credential: PhoneAuthCredential,auth: FirebaseAuth): Task<AuthResult> =
        userRepo.signInWithPhoneAuthCredential(credential,auth)
}