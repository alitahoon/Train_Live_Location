package com.example.repo

import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import com.example.data.*
import com.example.domain.entity.*
import com.example.domain.repo.UserRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.storage.StorageReference
import retrofit2.Response
import java.util.concurrent.TimeUnit

class userRepoImpl(
    private val apiService: ApiService,
    private val locationLive: LocationLive,
    private val locationTrackBackgroundService: LocationTrackBackgroundService,
    private val getLocationService: GetLocationService
) : UserRepo {
    private val TAG: String? = "userRepoImpl"
    override suspend fun getUserData(
        userPhone: String?,
        userPassword: String?
    ): Response<ArrayList<userResponseItem>> = apiService.getUserData(userPhone!!, userPassword!!)

    override suspend fun addNewUser(user: RegisterUser?): Response<ArrayList<userResponseItem>> =
        apiService.addNewUser(user)

    override suspend fun sendOtpToPhone(
        phoneNumber: String,
        auth: FirebaseAuth,
        activity: AppCompatActivity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
    ): PhoneAuthOptions {
        auth.setLanguageCode("ar")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        return options
    }

    override suspend fun resendOtpCode(
        phoneNumber: String,
        auth: FirebaseAuth,
        activity: AppCompatActivity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ): PhoneAuthOptions {
        auth.setLanguageCode("ar")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        return options
    }

    override suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        auth: FirebaseAuth
    ): Task<AuthResult> {
        return auth.signInWithCredential(credential)
    }

    override suspend fun sendProfileImageToFirebaseStorage(
        profileImagesUri: Uri,
        imageName: String,
        imageRef: StorageReference
    ) {
        profileImagesUri?.let {
            imageRef.child("profileImages/$imageName").putFile(it).addOnCompleteListener(
                OnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i(TAG, it.result.toString())
                    }
                }).addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
        }
    }

    override suspend fun startLocationUpdate() {
        locationLive.startLocationUpdate()
    }

    override suspend fun GetUserLocationLive(): LiveData<LocationDetails> {
        return locationLive
    }

    override suspend fun getLocationTrackBackgroundService(trainid: Int,userid:Int): LifecycleService {
//        locationTrackBackgroundService.setTrainId_userId(trainid,userid)
        return locationTrackBackgroundService
    }

    override suspend fun getLocationTrackForegroundService(trainid:Int): LifecycleService {
        getLocationService.setTrainID(trainid)
        return getLocationService
    }

    override suspend fun addLiveLoctationToApi(locationRequest: Location_Request): Response<Location_Request_with_id> =
        apiService.AddLocation(locationRequest)

    override suspend fun getLiveLoctationFromApi(trainid: Int): Response<Location_Response> =
        apiService.GetLocation(trainid)

    override suspend fun createPost(post: Post): Response<Post> = apiService.CreatePost()


}