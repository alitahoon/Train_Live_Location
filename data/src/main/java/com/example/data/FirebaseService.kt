package com.example.data

import android.app.Activity
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.storage.StorageReference

class FirebaseService(private val auth: FirebaseAuth) {
    suspend fun sendOtpToPhone(phoneNumber: String, phoneAuthOptions: PhoneAuthOptions){

    }

    suspend fun resendOtpCode(phoneNumber: String, phoneAuthOptions: PhoneAuthOptions){

    }

    suspend fun sendProfileImageToFirebaseStorage(
        profileImagesUri: Uri,
        imageName: String,
        imageRef: StorageReference
    ){

    }

    suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
    ){

    }

    suspend fun resendOtpCode(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ){

    }

}