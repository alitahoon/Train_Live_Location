package com.example.domain.repo

import android.app.Activity
import android.location.Location
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.domain.entity.LocationDetails
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.storage.StorageReference
import retrofit2.Call
import retrofit2.Response

interface UserRepo {
    suspend fun getUserData(
        userPhone: String?,
        userPassword: String?
    ): Response<ArrayList<userResponseItem>>

    suspend fun addNewUser(user: RegisterUser?): Response<ArrayList<userResponseItem>>
    suspend fun sendOtpToPhone(
        phoneNumber: String,
        auth: FirebaseAuth,
        activity: Activity,
        callbacks: OnVerificationStateChangedCallbacks
    ): PhoneAuthOptions

    suspend fun resendOtpCode(
        phoneNumber: String,
        auth: FirebaseAuth,
        activity: Activity,
        callbacks: OnVerificationStateChangedCallbacks
    ): PhoneAuthOptions

    suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        auth: FirebaseAuth
    ): Task<AuthResult>

    suspend fun sendProfileImageToFirebaseStorage(
        profileImagesUri: Uri,
        imageName: String,
        imageReference: StorageReference
    )

    suspend fun startLocationUpdate()
    suspend fun GetUserLocationLive(): LiveData<LocationDetails>

}