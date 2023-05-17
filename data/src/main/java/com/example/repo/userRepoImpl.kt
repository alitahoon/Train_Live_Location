package com.example.repo

import Resource
import android.location.Location
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
    private val firebaseService: FirebaseService,
    private val locationServices: LocationServices,
    private val sharedPreferencesService: SharedPreferencesService,
    private val getTrainForgroundService: GetTrainLocationService,
    private val getCurrantLocationJustOnce: GetCurrantLocationJustOnce,
    private val getCurrantLocationLive:GetCurrantLocationLive
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

    override suspend fun sendUserNotificationTokenToFirebase(
        token: NotificatonToken?,
        result: (Resource<String?>) -> Unit
    ) {
        firebaseService.sendUserNotificatonToken(token,result)
    }

    override suspend fun addNewUser(
        user: RegisterUser?,
        result: (Resource<UserResponseItem>) -> Unit
    ) {
        var res = apiService.addNewUser(user)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
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

    override suspend fun GetUserLocationLive(result: (LiveData<LocationDetails>) -> Unit) {
        result.invoke(locationLive)
    }

    override suspend fun getLocationTrackBackgroundService(
        trainid: Int,
        userid: Int,
        result: (Resource<LifecycleService>) -> Unit
    ) {
        locationTrackBackgroundService.setTrainId_userId(trainid,userid)
        result.invoke(Resource.Success(locationTrackBackgroundService))
    }

    override suspend fun getLocationTrackForegroundService(trainid: Int): LifecycleService {
        getLocationService.setTrainID(trainid)
        return getLocationService
    }

    override suspend fun addLiveLoctationToApi(
        locationRequest: Location_Request,
        result: (Resource<Location_Request_with_id>) -> Unit
    ) {
        var res = apiService.AddLocation(locationRequest)
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

    override suspend fun sendDoctorNotificationUsingFCM(
        token: NotificatonToken,
        serverKey: String?,
        doctorNotification: DoctorNotification,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.sendDoctorNotificationUsingFCM(token,serverKey!!,doctorNotification,result)
    }

    override suspend fun getLiveLoctationFromApi(
        trainid: Int,
        result: (Resource<Location_Response>) -> Unit
    ) {
        var res = apiService.GetLocation(trainid)
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

    override suspend fun getAllPostsFromAPI(result: (Resource<ArrayList<PostModelResponse>>) -> Unit) {
        var res = apiService.GetAllPosts()
        if (res.isSuccessful) {
            if (res.body() != null) {

                result.invoke(Resource.Success(ArrayList(res.body()!!.reversed().toMutableList())))
            } else {
                result.invoke((Resource.Failure("getAllPostsFromAPI -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getAllPostsFromAPI -> ${res.message()}")))
        }

    }

    override suspend fun pushAddPostCommentNotification(
        notification: PushPostCommentNotification,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.sendNewNotificationToAddedPostCommentTopic(notification,result)
    }


    override suspend fun getUserDataById(
        userID: Int,
        result: (Resource<UserResponseItem>) -> Unit
    ) {
        var res = apiService.GetUserById(userID)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke(Resource.Failure("getUserDataById -> Failer body is  ${res.body()}"))
            }
        } else {
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
        var res = apiService.CreateComment(commentRequest)
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
        var res = apiService.GetPostComments(postId)
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
        var res = apiService.DeletePost(postId!!)
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
        var res = apiService.GetStationById(stationId!!)
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

    override suspend fun updateUserData(
        userID: Int?,
        userRequest: RegisterUser,
        result: (Resource<String>) -> Unit
    ) {
        var res = apiService.UpdateUser(userRequest, userID!!)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success("${res.body()!!}"))
            } else {
                result.invoke((Resource.Failure("updateUserData -> Error response body = null :${res.body()}")))
            }
        } else {
            Log.i(TAG, "${userRequest}->>>>>>>>>${userID}")
            result.invoke((Resource.Failure("updateUserData -> ${res.message()}")))
        }
    }

    override suspend fun getInboxSentChatFromFirebase(
        phone: String?,
        result: (Resource<ArrayList<Message>>) -> Unit
    ) {
        firebaseService.getInboxSentChatFromFirebase(phone, result)
    }

    override suspend fun sendMessageToFirebasechat(
        message: String?,
        senderPhone: String?,
        reciverPhone: String?,
        senderUsername: String?,
        recieverUsername: String?,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.sendMessageToChat(message, senderPhone, reciverPhone,senderUsername,recieverUsername, result)
    }

    override suspend fun subscribeToNewTopic(
        topicInput: String,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.subscribeToNewTopic(topicInput,result)
    }

    override suspend fun getNews(result: (Response<ArrayList<GetNewsResponseItem>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun createNews(result: (Response<CreateNewsResponseItem>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getNewsById(
        newsId: Int,
        result: (Resource<GetNewsByIdResponseItem>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserCurrantLocationJustOnce(result: (Resource<Location>) -> Unit) {
        getCurrantLocationJustOnce.createLocationRequest()
        getCurrantLocationJustOnce.startGettingLocation(result)
    }

    override suspend fun getUserCurrantLocationLive(result: (Resource<Location>) -> Unit) {
        getCurrantLocationLive.createLocationRequest()
        getCurrantLocationLive.startGettingLocation(result)
    }

    override suspend fun pushAddPostNotification(
        notification: PushPostNotification,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.sendNewNotificationToAddedPostTopic(notification,result)
    }

    override suspend fun pushNewTopicNotification(
        notification: PushNotification,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.sendNewNotificationToTopic(notification,result)
    }

    override suspend fun getDataFromSharedPrefrences(
        sharedPrefFile: String?,
        result: (Resource<UserResponseItem>) -> Unit
    ) {
        sharedPreferencesService.getUserData(sharedPrefFile, result)
    }

    override suspend fun createTicket(
        ticketRequestItem: TicketRequestItem,
        result: (Resource<TicketResponseItem>) -> Unit
    ) {
        var res = apiService.CreateTicket(ticketRequestItem)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("createTicket -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("createTicket -> ${res.message()}")))
        }
    }

    override suspend fun getUserNotificationInStation(
        stationId: Int,
        result: (Resource<ArrayList<StationUsersNotificationResponseItem>>) -> Unit
    ) {
        var res = apiService.GetUserNotificationInStation(stationId!!)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("GetUserNotificationInStation -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("GetUserNotificationInStation -> ${res.message()}")))
        }
    }

    override suspend fun getUserInTrain(
        trainId: Int,
        result: (Resource<ArrayList<UserInTrainResponseItem>>) -> Unit
    ) {
        var res = apiService.GetUserInTrain(trainId!!)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("GetUserInTrain -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("GetUserInTrain -> ${res.message()}")))
        }
    }

    override suspend fun getDoctorInTrain(trainId:Int?, result: (Resource<ArrayList<DoctorResponseItem>>) -> Unit) {
        var res = apiService.GetDoctors(trainId!!)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("getDoctorInTrain -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getDoctorInTrain -> ${res.message()}")))
        }
    }

    override suspend fun getTrainLocationInForgroundService(
        trainId: Int?,
        result: (Resource<LifecycleService>) -> Unit
    ) {
        getTrainForgroundService.setTrainId(trainId)
        result.invoke(Resource.Success(getTrainForgroundService))
    }

    override suspend fun getInboxRecieveChatFromFirebase(
        phone: String?,
        result: (Resource<ArrayList<Message>>) -> Unit
    ) {
        firebaseService.getInboxRecieveChatFromFirebase(phone, result)
    }

    override suspend fun getChatFromFirebase(
        senderPhone: String?,
        recieverPhone: String?,
        result: (Resource<ArrayList<Message>>) -> Unit
    ) {
        firebaseService.getChatMessagesFromFirebase(senderPhone, recieverPhone, result)
    }


    override suspend fun getUserLocation(result: (Resource<LocationDetails>) -> Unit) =
        getUserLocation.getLocationWithLocationManger(result)

    override suspend fun sendDoctorNotificationToFirebase(
        doctoreNotification: DoctorNotification,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.sendDoctorNotification(doctoreNotification,result)
    }

    override suspend fun getDoctorNotificationFromFirebase(
        userPhone: String,
        result: (Resource<ArrayList<DoctorNotification>>) -> Unit
    ) {
        firebaseService.getNotificationFromFirebase(userPhone,result)
    }

    override suspend fun getNotificationTokenFromFirebase(
        userPhone: String?,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.getNotificationToken(userPhone!!,result)
    }
}