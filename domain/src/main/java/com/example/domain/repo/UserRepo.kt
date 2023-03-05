package com.example.domain.repo

import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import com.example.domain.entity.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.storage.StorageReference
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
        activity: AppCompatActivity,
        callbacks: OnVerificationStateChangedCallbacks
    ): PhoneAuthOptions

    suspend fun resendOtpCode(
        phoneNumber: String,
        auth: FirebaseAuth,
        activity: AppCompatActivity,
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

    suspend fun getLocationTrackBackgroundService(trainid: Int,userid:Int):LifecycleService
    suspend fun getLocationTrackForegroundService(trainid:Int):LifecycleService

    suspend fun addLiveLoctationToApi(locationRequest: Location_Request):Response<Location_Request_with_id>
    suspend fun getLiveLoctationFromApi(trainid:Int):Response<Location_Response>

}