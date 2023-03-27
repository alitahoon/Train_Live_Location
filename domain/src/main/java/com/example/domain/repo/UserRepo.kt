package com.example.domain.repo

import android.app.Activity
import android.location.Location
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
        phoneNumber: String?,
        callback: (result: String?) -> Unit
    )

    suspend fun resendOtpCode(
        phoneNumber: String?,
        callback: (result: String?) -> Unit
    )

    suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        callback: (result: String?) -> Unit
    )

    suspend fun createAPhoneAuthCredential(
        code: String?,
        callback: (result: PhoneAuthCredential?) -> Unit
    )

    suspend fun sendImageToFirebaseStorage(
        profileImagesUri: Uri,
        imagePath: String,
        callback: (result: String?) -> Unit
    )

    suspend fun stopLocationUpdate()
    suspend fun startLocationUpdate(interval: Long?)
    suspend fun GetUserLocationLive(): LiveData<LocationDetails>

    suspend fun getLocationTrackBackgroundService(trainid: Int, userid: Int): LifecycleService
    suspend fun getLocationTrackForegroundService(trainid: Int): LifecycleService

    suspend fun addLiveLoctationToApi(locationRequest: Location_Request): Response<Location_Request_with_id>
    suspend fun getLiveLoctationFromApi(trainid: Int): Response<Location_Response>

    suspend fun getUserLocation(callback: (LocationDetails) -> Unit)

    suspend fun createPost(post: Post): Response<PostModelResponse>
    suspend fun getAllPostsFromAPI(): Response<ArrayList<Post>>


    suspend fun getUserDataById(userID: Int): Response<userResponseItem>

    suspend fun setFirebaseServiceActivity(activity: AppCompatActivity)
}