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
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.*
import com.google.maps.model.DirectionsResult
import retrofit2.Response

class userRepoImpl(
    private val apiService: ApiService,
    private val locationLive: LocationLive,
    private val getLocationService: GetLocationService,
    private val getUserLocation: userLocation,
    private val firebaseService: FirebaseService,
    private val locationServices: LocationServices,
    private val sharedPreferencesService: SharedPreferencesService,
    private val getTrainForgroundService: GetTrainLocationService,
    private val getCurrantLocationJustOnce: GetCurrantLocationJustOnce,
    private val getCurrantLocationLive: GetCurrantLocationLive,
    private val myDatabase: MyDatabase,
    private val googleMapsServices: GoogleMapsServices
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

    override suspend fun getStationAlarmsFromDatabase(result: (Resource<ArrayList<StationAlarmEntity>>) -> Unit) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.stationAlarmDao().getAllStationAlarmEntity()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting StationAlarmsFromDatabase ---> ${e.message}"))
        }
    }

    override suspend fun clearUserSignDataFromDatabase(result: (Resource<String>) -> Unit) {
        try {
            myDatabase.UserSignInDataEntityDao().clear()
            result.invoke(
                Resource.Success(
                   "Successfully clear user data"
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting StationAlarmsFromDatabase ---> ${e.message}"))
        }    }

    override suspend fun clearDirectionRouteFromDatabase(result: (Resource<String>) -> Unit) {
        try {
            myDatabase.RouteDirctionsEntityDao().clear()
            result.invoke(
                Resource.Success(
                    "Successfully clear route data"
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed clear RouteFromDatabase ---> ${e.message}"))
        }
    }

    override suspend fun insertNewUserItemToDatabase(
        userItemEntity: UserItemEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.UserItemEntityDao().insertUserItemEntity(userItemEntity)
            result.invoke(Resource.Success("Successfully added user data item in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting UserItemToDatabase ---> ${e.message}"))
        }
    }

    override suspend fun updateStationAlarmFromDatabase(
        stationAlarmEntity: StationAlarmEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.stationAlarmDao().updateItem(stationAlarmEntity)
            result.invoke(Resource.Success("Successfully updated station Alarm data in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed updating stationAlarmEntity.. ---> ${e.message}"))
        }
    }

    override suspend fun gettingTrainlocationFromApi(
        trainId: Int,
        result: (Resource<Location_Response>) -> Unit
    ) {
        var res = apiService.GetLocation(trainId)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke(Resource.Failure("Error while getting train Location body is null:${res.body()}"))
            }
        } else {
            result.invoke(Resource.Failure("${res.message()}:${res.errorBody()}"))
        }
    }

    override suspend fun deleteStationAlarmFromDatabase(
        alarmID: Long,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.stationAlarmDao().deleteItemById(alarmID)
            result.invoke(Resource.Success("Successfully deleted alarm"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed deleting alarm---> ${e.message}"))
        }
    }

    override suspend fun getNewUserItemFromDatabase(result: (Resource<ArrayList<UserItemEntity>>) -> Unit) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.UserItemEntityDao().getAllUserItemEntity()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting UserItemFromDatabase ---> ${e.message}"))
        }
    }

    override suspend fun getNewsItemFromDatabase(result: (Resource<ArrayList<NewsItemEntity>>) -> Unit) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.NewsItemEntityDao().getAllNewsItemEntity()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting NewsItemFromDatabase ---> ${e.message}"))
        }
    }

    override suspend fun insertNewsItemToDatabase(
        newsItemEntity: NewsItemEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.NewsItemEntityDao().insertNewsItemEntity(newsItemEntity)
            result.invoke(Resource.Success("Successfully added news data item in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting NewsItemToDatabase ---> ${e.message}"))
        }
    }

    override suspend fun getAllUserSignInDataEntity(result: (Resource<ArrayList<UserSignInDataEntity>>) -> Unit) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.UserSignInDataEntityDao().getAllUserSignInDataEntity()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting UserSignInDataEntity ---> ${e.message}"))
        }
    }

    override suspend fun insertUserSignInDataEntity(
        userSignInDataEntity: UserSignInDataEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.UserSignInDataEntityDao().insertUserSignInDataEntity(userSignInDataEntity)
            result.invoke(Resource.Success("Successfully added user sign in data in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting UserSignInDataEntity ---> ${e.message}"))
        }
    }

    override suspend fun insertLocationItemToDatabase(
        locationItemEntity: LocationItemEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.LocationItemEntityDao().insertLocationItemEntity(locationItemEntity)
            result.invoke(Resource.Success("Successfully added Location data item in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting LocationItemToDatabase ---> ${e.message}"))
        }
    }

    override suspend fun updateLocationItemFromDatabase(
        locationItemEntity: LocationItemEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.LocationItemEntityDao().updateLocationItemEntity(locationItemEntity)
            result.invoke(Resource.Success("Successfully updated location data in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed updating locationItemEntity.. ---> ${e.message}"))
        }
    }

    override suspend fun getDirctionRoutesFromDatabase(result: (Resource<ArrayList<RouteDirctionEntity>>) -> Unit) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.RouteDirctionsEntityDao().getAllTrainItemEntity()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting Route Dirctions Items ---> ${e.message}"))
        }    }

    override suspend fun deleteLocationItemFromDatabase(
        locationID: Long,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.LocationItemEntityDao().deleteLocationItemEntity(locationID)
            result.invoke(Resource.Success("Successfully deleted location"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed deleting location---> ${e.message}"))
        }
    }

    override suspend fun insertNewTrainItemToDatabase(
        trainItemEntity: TrainItemEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.TrainItemEntityDao().insertTrainItemEntity(trainItemEntity)
            result.invoke(Resource.Success("Successfully added train data item in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting TrainItemToDatabase ---> ${e.message}"))
        }
    }

    override suspend fun getNewTrainItemFromDatabase(result: (Resource<ArrayList<TrainItemEntity>>) -> Unit) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.TrainItemEntityDao().getAllTrainItemEntity()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting TrainItemFromDatabase ---> ${e.message}"))
        }
    }

    override suspend fun insertNewTicketItemToDatabase(
        ticketItemEntity: TicketItemEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.TicketItemEntityDao().insertTicketItemEntity(ticketItemEntity)
            result.invoke(Resource.Success("Successfully added ticket data item in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting TicketItemToDatabase ---> ${e.message}"))
        }
    }

    override suspend fun getNewTicketItemFromDatabase(result: (Resource<ArrayList<TicketItemEntity>>) -> Unit) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.TicketItemEntityDao().getAllTicketItemEntity()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting TicketItemFromDatabase ---> ${e.message}"))
        }
    }

    override suspend fun insertNewMessageItemToDatabase(
        messageItemEntity: MessageItemEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.MessageItemEntityDao().insertMessageItemEntity(messageItemEntity)
            result.invoke(Resource.Success("Successfully added message data item in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting MessageItemToDatabase ---> ${e.message}"))
        }
    }

    override suspend fun getNewMessageItemFromDatabase(result: (Resource<ArrayList<MessageItemEntity>>) -> Unit) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.MessageItemEntityDao().getAllMessageItemEntity()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting MessageItemFromDatabase ---> ${e.message}"))
        }
    }

    override suspend fun insertnewDirctionRouteInDatabase(
        routeDirctionEntity: RouteDirctionEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.RouteDirctionsEntityDao().insertTrainItemEntity(routeDirctionEntity)
            result.invoke(Resource.Success("Successfully added route data item in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting Route Item To Database ---> ${e.message}"))
        }    }

    override suspend fun insertNewStationAlarm(
        stationAlarmEntity: StationAlarmEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.stationAlarmDao().insertStationAlarmEntity(stationAlarmEntity)
            result.invoke(Resource.Success("Insert stationAlarmEntity data success"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Error wihle inserting stationAlarmEntity data ---> ${e.message}"))
        }
    }

    override suspend fun sendUserNotificationTokenToFirebase(
        token: NotificatonToken?,
        result: (Resource<String?>) -> Unit
    ) {
        firebaseService.sendUserNotificatonToken(token, result)
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
            result.invoke(Resource.Failure("${res.message()}:${res.raw()}"))
        }
    }

    override suspend fun insertNewStationHistroyItemToDatabase(
        stationHistoryAlarmEntity: StationHistoryAlarmEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.StationHistoryAlarmDao()
                .insertStationHistoryAlarmEntity(stationHistoryAlarmEntity)
            result.invoke(Resource.Success("Successfully added stationHistoryAlarmEntity data item in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting stationHistoryAlarmEntity ---> ${e.message}"))
        }
    }

    override suspend fun getStationHistroyItemsFromDatabase(
        result: (Resource<ArrayList<StationHistoryAlarmEntity>>) -> Unit
    ) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.StationHistoryAlarmDao().getStationHistoryAlarmEntity()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting getStationHistoryAlarmsEntity... ---> ${e.message}"))
        }
    }

    override suspend fun sendOtpToPhone(
        phoneNumber: String?,
        result: (result: Resource<String>) -> Unit
    ) = firebaseService.sendOtpToPhone(phoneNumber, result)

    override suspend fun resendOtpCode(
        phoneNumber: String?,
        result: (result: Resource<String>) -> Unit
    ) {
        firebaseService.resendOtpCode(phoneNumber, result)
    }


    override suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        callback: (result: String?) -> Unit
    ) = firebaseService.signInWithPhoneAuthCredential(credential, callback)

    override suspend fun getNotificationTokenByUserIDFromApi(
        userId: Int,
        result: (Resource<NotificationTokenResponse>) -> Unit
    ) {
        var res = apiService.GetUserTokenById(userId)
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

    override suspend fun getNotificationTokenForUsersInTrain(
        trainId: Int,
        result: (Resource<ArrayList<NotificationTokenResponseInTrain>>) -> Unit
    ) {
        var res = apiService.GetUsersTokenInTrain(trainId)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("getNotificationTokenForUsersInTrain -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getNotificationTokenForUsersInTrain -> ${res.message()}")))
        }
    }


    override suspend fun reportPost(
        postId: Int,
        userID: Int,
        reportReason: String,
        result: (Resource<String>) -> Unit
    ) {
        var res = apiService.CreatePostRepor(ReportPostRequset(reportReason, postId, userID))
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("reportPost -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("reportPost -> ${res.message()}")))
        }
    }

    override suspend fun getAllReport(
        result: (Resource<ArrayList<ReportPostResponse>>) -> Unit
    ) {
        var res = apiService.GetAllReports()
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("getAllReport -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getAllReport -> ${res.message()}")))
        }
    }

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
        firebaseService.sendDoctorNotificationUsingFCM(
            token,
            serverKey!!,
            doctorNotification,
            result
        )
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
        firebaseService.sendNewNotificationToAddedPostCommentTopic(notification, result)
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

    override suspend fun createUserNotificationToken(result: (Resource<String>) -> Unit) {
        firebaseService.createUserNotificationToken(result)
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
                result.invoke(Resource.Success("${res.message()!!}"))
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
        firebaseService.sendMessageToChat(
            message,
            senderPhone,
            reciverPhone,
            senderUsername,
            recieverUsername,
            result
        )
    }

    override suspend fun subscribeToNewTopic(
        topicInput: String,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.subscribeToNewTopic(topicInput, result)
    }

    override suspend fun getNews(result: (Resource<ArrayList<GetNewsResponseItem>>) -> Unit) {
        var res = apiService.GetNews()
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("getNews -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getNews -> ${res.message()}")))
        }
    }

    override suspend fun createNews(result: (Resource<CreateNewsResponseItem>) -> Unit) {

        TODO("Not yet implemented")
    }

    override suspend fun getNewsById(
        newsId: Int,
        result: (Resource<GetNewsByIdResponseItem>) -> Unit
    ) {
        var res = apiService.GetNewsById(newsId)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(Resource.Success(res.body()!!))
            } else {
                result.invoke((Resource.Failure("getNewsById -> Error response body = null :${res.body()}")))
            }
        } else {
            result.invoke((Resource.Failure("getNewsById -> ${res.message()}")))
        }
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
        firebaseService.sendNewNotificationToAddedPostTopic(notification, result)
    }

    override suspend fun getLocationDirctionFromGoogleMapsApi(
        origin: LatLng,
        destination: LatLng,
        result: (Resource<DirectionsResult>) -> Unit
    ) {
        googleMapsServices.getDirections(origin, destination, result)
    }

    override suspend fun getLocationDirctionFromOpenRouteService(
        origin: LatLng,
        destination: LatLng,
        result: (Resource<OpenRouteDirectionResult>) -> Unit
    ) {
        googleMapsServices.getDirectionFromOpenRoute(origin, destination, result)
    }

    override suspend fun getWayPointsLocationDirctionFromOpenRouteService(
        wayPoints: List<LatLng>,
        result: (Resource<OpenRouteDirectionResult>) -> Unit
    ) {
        googleMapsServices.getDirectionFromOpenRouteForWaypoints(wayPoints, result)
    }

    override suspend fun pushSendMessageNotification(
        notification: PushMessageNotification,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.sendNewNotificationToMessageTopic(notification, result)
    }

    override suspend fun pushNewTopicNotification(
        notification: PushNotification,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.sendNewNotificationToTopic(notification, result)
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

    override suspend fun getDoctorInTrain(
        trainId: Int?,
        result: (Resource<ArrayList<DoctorResponseItem>>) -> Unit
    ) {
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
        firebaseService.sendDoctorNotification(doctoreNotification, result)
    }

    override suspend fun getDoctorNotificationFromFirebase(
        userPhone: String,
        result: (Resource<ArrayList<DoctorNotification>>) -> Unit
    ) {
        firebaseService.getNotificationFromFirebase(userPhone, result)
    }

    override suspend fun getAllStationsFromDatabase(result: (Resource<ArrayList<StationItemEntity>>) -> Unit) {
        try {
            result.invoke(
                Resource.Success(
                    ArrayList(
                        myDatabase.StationItemEntityDao().getStationItemEntityDao()
                    )
                )
            )
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting Station Items Entity ---> ${e.message}"))
        }    }

    override suspend fun insertnewStationToDatabase(
        stationItemEntity: StationItemEntity,
        result: (Resource<String>) -> Unit
    ) {
        try {
            myDatabase.StationItemEntityDao().insertStationItemEntityDao(stationItemEntity)
            result.invoke(Resource.Success("Successfully added station data item in database"))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed inserting station item ---> ${e.message}"))
        }    }


    override suspend fun getNotificationTokenFromFirebase(
        userPhone: String?,
        result: (Resource<String>) -> Unit
    ) {
        firebaseService.getNotificationToken(userPhone!!, result)
    }
}