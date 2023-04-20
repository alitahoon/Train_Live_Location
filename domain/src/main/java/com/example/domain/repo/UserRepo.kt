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
    suspend fun GetUserLocationLive(result:(LiveData<LocationDetails>)->Unit)

    suspend fun getLocationTrackBackgroundService(trainid: Int, userid: Int): LifecycleService
    suspend fun getLocationTrackForegroundService(trainid: Int): LifecycleService

    suspend fun addLiveLoctationToApi(locationRequest: Location_Request,result: (Resource<Location_Request_with_id>) -> Unit)
    suspend fun getLiveLoctationFromApi(trainid: Int,result:(Resource<Location_Response>)->Unit)

    suspend fun getUserLocation(callback: (LocationDetails) -> Unit)

    suspend fun createPost(post: Post,result: (Resource<PostModelResponse>) -> Unit)
    suspend fun getAllPostsFromAPI(result: (Resource<ArrayList<PostModelResponse>>)->Unit)


    suspend fun getUserDataById(userID: Int,result: (Resource<userResponseItem>) -> Unit)

    suspend fun setFirebaseServiceActivity(activity: AppCompatActivity)

    suspend fun getAllStations(result: (Resource<ArrayList<StationResponseItem>>) -> Unit)
    suspend fun getAllTrains(result: (Resource<ArrayList<TrainResponseItem>>) -> Unit)

    suspend fun getImageFromFirebaseStorage(imageRef:String?,result: (Resource<Uri>) -> Unit)

    suspend fun createPostComment(commentRequest: CommentRequest,result: (Resource<CommentResponse>) -> Unit)
    suspend fun getCommentsForPostUsingId(postId:Int?,result: (Resource<ArrayList<PostCommentsResponseItem>>)->Unit)
    suspend fun deletePostWithID(postId:Int?,result: (Resource<String>)->Unit)
}