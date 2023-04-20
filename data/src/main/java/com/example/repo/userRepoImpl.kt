package com.example.repo

import Resource
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import com.example.data.*
import com.example.domain.entity.*
import com.example.domain.repo.UserRepo
import com.google.android.gms.location.LocationRequest
import com.google.firebase.auth.*

class userRepoImpl(
    private val apiService: ApiService,
    private val locationLive: LocationLive,
    private val locationTrackBackgroundService: LocationTrackBackgroundService,
    private val getLocationService: GetLocationService,
    private val getUserLocation: userLocation,
    private val firebaseService: FirebaseService,
    private val locationServices: LocationServices
) : UserRepo {
    private val TAG: String? = "userRepoImpl"
    override suspend fun getUserData(
        userPhone: String?,
        userPassword: String?,
        result: (Resource<UserResponseItem>) -> Unit
    ) {
        val res = apiService.getUserData(userPhone!!, userPassword!!)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke(Resource.Failure("Error Body is null:${res.body()}"))
            }
        } else {
            result.invoke(Resource.Failure("failed:${res.message()}"))
        }
    }

    override suspend fun addNewUser(
        user: RegisterUser?,
        result: (Resource<UserResponseItem>) -> Unit
    ) {
        var res = apiService.addNewUser(user)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            }else{
                result.invoke(Resource.Failure("Error Body is null:${res.body()}"))
            }
        } else {
            result.invoke(Resource.Failure("${res.message()}:${res.errorBody()}"))
        }
    }


    override suspend fun sendOtpToPhone(
        phoneNumber: String?,
        callback: (result: String?) -> Unit
    ) = firebaseService.sendOtpToPhone(phoneNumber, callback)

    override suspend fun resendOtpCode(
        phoneNumber: String?,
        callback: (result: String?) -> Unit

    ) = firebaseService.resendOtpCode(phoneNumber!!, callback)

    override suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        callback: (result: String?) -> Unit
    ) = firebaseService.signInWithPhoneAuthCredential(credential, callback)

    override suspend fun createAPhoneAuthCredential(
        code: String?,
        callback: (result: PhoneAuthCredential?) -> Unit
    ) = firebaseService.CreateAPhoneAuthCredentialWithCode(code, callback)

    override suspend fun sendImageToFirebaseStorage(
        profileImagesUri: Uri,
        imagePath: String,
        result: (Resource<String>) -> Unit
    ) = firebaseService.sendImageToFirebaseStorage(profileImagesUri, imagePath, result)

    override suspend fun stopLocationUpdate() = locationLive.stopLocationLiveUpdate()

    override suspend fun startLocationUpdate(interval: Long?) {
        var locationRequest = LocationRequest.create()
        locationRequest?.interval = interval!!
        locationRequest?.fastestInterval = interval!! / 4
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationLive.startLocationUpdate(locationRequest)
    }

    override suspend fun GetUserLocationLive(result:(LiveData<LocationDetails>)->Unit) {
        result.invoke(locationLive)
    }

    override suspend fun getLocationTrackBackgroundService(
        trainid: Int,
        userid: Int
    ): LifecycleService {
//        locationTrackBackgroundService.setTrainId_userId(trainid,userid)
        return locationTrackBackgroundService
    }

    override suspend fun getLocationTrackForegroundService(trainid: Int): LifecycleService {
        getLocationService.setTrainID(trainid)
        return getLocationService
    }

    override suspend fun addLiveLoctationToApi(locationRequest: Location_Request,result: (Resource<Location_Request_with_id>) -> Unit){
        var res=apiService.AddLocation(locationRequest)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("addLiveLoctationToApi -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("addLiveLoctationToApi -> ${res.message()}")))
        }

    }

    override suspend fun getLiveLoctationFromApi(trainid: Int,result:(Resource<Location_Response>)->Unit){
        var res=apiService.GetLocation(trainid)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("getLiveLoctationFromApi -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getLiveLoctationFromApi -> ${res.message()}")))
        }
    }
    override suspend fun createPost(post: Post, result: (Resource<PostModelResponse>) -> Unit) {
        var res = apiService.CreatePost(post)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("createPost -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("createPost -> ${res.message()}")))
        }

    }

    override suspend fun getAllPostsFromAPI(result: (Resource<ArrayList<PostModelResponse>>)->Unit) {
        var res = apiService.GetAllPosts()
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("getAllPostsFromAPI -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getAllPostsFromAPI -> ${res.message()}")))
        }

    }





    override suspend fun getUserDataById(userID: Int,result: (Resource<UserResponseItem>) -> Unit) {
        var res =apiService.GetUserById(userID)
        if (res.isSuccessful){
            if (res.body()!=null){
                result.invoke(Resource.Success(res.body()!!))
            }else{
                result.invoke(Resource.Failure("getUserDataById -> Failer body is  ${res.body()}"))
            }
        }else{
            result.invoke(Resource.Failure("getUserDataById -> Failer ${res.message()}"))
        }
    }


    override suspend fun setFirebaseServiceActivity(activity: AppCompatActivity) {
        Log.i(TAG, "setFirebaseServiceActivity")
        firebaseService.setActivity(activity)
    }

    override suspend fun getAllStations(result: (Resource<ArrayList<StationResponseItem>>) -> Unit) {
        var res = apiService.GetAllStation()
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            }
        } else {
            result.invoke(Resource.Failure(res.message()))
        }
    }

    override suspend fun getAllTrains(result: (Resource<ArrayList<TrainResponseItem>>) -> Unit) {
        var res = apiService.GetAllTrains()
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            }
        } else {
            result.invoke(Resource.Failure(res.message()))
        }
    }

    override suspend fun getImageFromFirebaseStorage(
        imageRef: String?,
        result: (Resource<Uri>) -> Unit
    ) {
        firebaseService.getImageFromFireBaseStorage(imageRef, result)
    }

    override suspend fun createPostComment(
        commentRequest: CommentRequest,
        result: (Resource<CommentResponse>) -> Unit
    ) {
        var res=apiService.CreateComment(commentRequest)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("createPostComment -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("createPostComment -> ${res.message()}")))
        }
    }

    override suspend fun getCommentsForPostUsingId(
        postId: Int?,
        result: (Resource<ArrayList<PostCommentsResponseItem>>) -> Unit
    ) {
        var res=apiService.GetPostComments(postId)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("getCommentsForPostUsingId -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getCommentsForPostUsingId -> ${res.message()}")))
        }
    }

    override suspend fun deletePostWithID(postId: Int?, result: (Resource<String>) -> Unit) {
        var res=apiService.DeletePost(postId!!)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success("${res.body()!!}"))
            } else {
                result.invoke((Resource.Failure("deletePostWithID -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("deletePostWithID -> ${res.message()}")))
        }
    }

    override suspend fun getStationById(
        stationId: Int?,
        result: (Resource<StationResponseItem>) -> Unit
    ) {
        var res=apiService.GetStationById(stationId!!)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("getStationById -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getStationById -> ${res.message()}")))
        }
    }


    override suspend fun getUserLocation(callback: (LocationDetails) -> Unit) =
        getUserLocation.getLocationWithLocationManger(callback)!!


}