package com.example.domain.repo

import Resource
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import com.example.domain.entity.*
import com.google.firebase.auth.PhoneAuthCredential
import retrofit2.Response

interface UserRepo {
    suspend fun getUserData(
        userPhone: String?,
        userPassword: String?, result: (Resource<userResponseItem>) -> Unit
    )

    suspend fun addNewUser(user: RegisterUser?, result: (Resource<userResponseItem>) -> Unit)
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
        result: (Resource<String>) -> Unit
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

    suspend fun getAllStations(result: (Resource<ArrayList<StationResponseItem>>) -> Unit)

    suspend fun getImageFromFirebaseStorage(imageRef:String?,result: (Resource<Uri>) -> Unit)
}