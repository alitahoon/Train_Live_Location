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
import retrofit2.Response

class userRepoImpl(
    private val apiService: ApiService,
    private val locationLive: LocationLive,
    private val locationTrackBackgroundService: LocationTrackBackgroundService,
    private val getLocationService: GetLocationService,
    private val getUserLocation: userLocation,
    private val firebaseService: FirebaseService
) : UserRepo {
    private val TAG: String? = "userRepoImpl"
    override suspend fun getUserData(
        userPhone: String?,
        userPassword: String?
    ): Response<userResponseItem> = apiService.getUserData(userPhone!!, userPassword!!)

    override suspend fun addNewUser(user: RegisterUser?, result: (Resource<userResponseItem>)->Unit) {
       var res= apiService.addNewUser(user)
        if (res.isSuccessful){
            if (res.body()!=null){
                result.invoke(Resource.Success(res.body()!!))
            }
        }else{
                result.invoke(Resource.Failure(res.message()))
        }
    }


    override suspend fun sendOtpToPhone(
        phoneNumber: String?,
        callback: (result: String?) -> Unit
    )=firebaseService.sendOtpToPhone(phoneNumber,callback)

    override suspend fun resendOtpCode(
        phoneNumber: String?,
        callback: (result: String?) -> Unit

    )=firebaseService.resendOtpCode(phoneNumber!!,callback)

    override suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        callback: (result: String?) -> Unit
    )=firebaseService.signInWithPhoneAuthCredential(credential,callback)

    override suspend fun createAPhoneAuthCredential(
        code: String?,
        callback: (result: PhoneAuthCredential?) -> Unit
    )=firebaseService.CreateAPhoneAuthCredentialWithCode(code,callback)

    override suspend fun sendImageToFirebaseStorage(
        profileImagesUri: Uri,
        imagePath: String,
        callback: (result: String?) -> Unit
    ) =firebaseService.sendImageToFirebaseStorage(profileImagesUri,imagePath,callback)

    override suspend fun stopLocationUpdate() = locationLive.stopLocationLiveUpdate()

    override suspend fun startLocationUpdate(interval:Long?) {
        var locationRequest = LocationRequest.create()
        locationRequest?.interval = interval!!
        locationRequest?.fastestInterval = interval!! / 4
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationLive.startLocationUpdate(locationRequest)
    }

    override suspend fun GetUserLocationLive(): LiveData<LocationDetails> {
        return locationLive
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

    override suspend fun addLiveLoctationToApi(locationRequest: Location_Request): Response<Location_Request_with_id> =
        apiService.AddLocation(locationRequest)

    override suspend fun getLiveLoctationFromApi(trainid: Int): Response<Location_Response> =
        apiService.GetLocation(trainid)

    override suspend fun createPost(post: Post): Response<PostModelResponse> =
        apiService.CreatePost(post)

    override suspend fun getAllPostsFromAPI(): Response<ArrayList<Post>> = apiService.GetAllPosts()


    override suspend fun getUserDataById(userID: Int): Response<userResponseItem> =
        apiService.GetUserById(userID)

    override suspend fun setFirebaseServiceActivity(activity: AppCompatActivity) {
        Log.i(TAG,"setFirebaseServiceActivity")
        firebaseService.setActivity(activity)
    }

    override suspend fun getAllStations(result: (Resource<ArrayList<StationResponseItem>>) -> Unit) {
        var res=apiService.GetAllStation()
        if (res.isSuccessful){
            if (res.body()!=null){
                result.invoke(Resource.Success(res.body()!!))
            }
        }else{
            result.invoke(Resource.Failure(res.message()))
        }
    }


    override suspend fun getUserLocation(callback: (LocationDetails) -> Unit) =
        getUserLocation.getLocationWithLocationManger(callback)!!


}