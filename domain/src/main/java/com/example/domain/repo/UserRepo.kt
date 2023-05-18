package com.example.domain.repo

import Resource
import android.location.Location
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
        userPassword: String?, result: (Resource<UserResponseItem>) -> Unit
    )

    suspend fun getNews(
        result: (Response<ArrayList<GetNewsResponseItem>>) -> Unit
    )

    suspend fun createNews(
        result: (Response<CreateNewsResponseItem>) -> Unit
    )

    suspend fun getNewsById(newsId: Int, result: (Resource<GetNewsByIdResponseItem>) -> Unit)

    suspend fun addNewUser(user: RegisterUser?, result: (Resource<UserResponseItem>) -> Unit)
    suspend fun sendOtpToPhone(
        phoneNumber: String?,
        result: (result: Resource<String>) -> Unit
    )

    suspend fun resendOtpCode(
        phoneNumber: String?,
        result: (result: Resource<String>) -> Unit
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
    suspend fun GetUserLocationLive(result: (LiveData<LocationDetails>) -> Unit)

    suspend fun getLocationTrackBackgroundService(
        trainid: Int, userid: Int, result: (Resource<LifecycleService>) -> Unit
    )

    suspend fun getLocationTrackForegroundService(trainid: Int): LifecycleService

    suspend fun addLiveLoctationToApi(
        locationRequest: Location_Request,
        result: (Resource<Location_Request_with_id>) -> Unit
    )

    suspend fun getLiveLoctationFromApi(trainid: Int, result: (Resource<Location_Response>) -> Unit)

    suspend fun getUserLocation(result: (Resource<LocationDetails>) -> Unit)

    suspend fun createPost(post: Post, result: (Resource<PostModelResponse>) -> Unit)
    suspend fun getAllPostsFromAPI(result: (Resource<ArrayList<PostModelResponse>>) -> Unit)


    suspend fun getUserDataById(userID: Int, result: (Resource<UserResponseItem>) -> Unit)

    suspend fun setFirebaseServiceActivity(activity: AppCompatActivity)

    suspend fun getAllStations(result: (Resource<ArrayList<StationResponseItem>>) -> Unit)
    suspend fun getAllTrains(result: (Resource<ArrayList<TrainResponseItem>>) -> Unit)

    suspend fun getImageFromFirebaseStorage(imageRef: String?, result: (Resource<Uri>) -> Unit)

    suspend fun createPostComment(
        commentRequest: CommentRequest,
        result: (Resource<CommentResponse>) -> Unit
    )

    suspend fun getCommentsForPostUsingId(
        postId: Int?,
        result: (Resource<ArrayList<PostCommentsResponseItem>>) -> Unit
    )

    suspend fun deletePostWithID(postId: Int?, result: (Resource<String>) -> Unit)
    suspend fun getStationById(stationId: Int?, result: (Resource<StationResponseItem>) -> Unit)
    suspend fun updateUserData(
        userID: Int?,
        userRequest: RegisterUser, result: (Resource<String>) -> Unit
    )

    suspend fun sendMessageToFirebasechat(
        message: String?,
        senderPhone: String?,
        reciverPhone: String?,
        senderUsername: String?,
        recieverUsername: String?,
        result: (Resource<String>) -> Unit
    )

    suspend fun getChatFromFirebase(
        senderPhone: String?,
        recieverPhone: String?,
        result: (Resource<ArrayList<Message>>) -> Unit
    )

    suspend fun getInboxRecieveChatFromFirebase(
        phone: String?,
        result: (Resource<ArrayList<Message>>) -> Unit
    )

    suspend fun getInboxSentChatFromFirebase(
        phone: String?,
        result: (Resource<ArrayList<Message>>) -> Unit
    )

    suspend fun getDataFromSharedPrefrences(
        sharedPrefFile: String?,
        result: (Resource<UserResponseItem>) -> Unit
    )

    suspend fun createTicket(
        ticketRequestItem: TicketRequestItem,
        result: (Resource<TicketResponseItem>) -> Unit
    )

    suspend fun getDoctorInTrain(
        trainId: Int?,
        result: (Resource<ArrayList<DoctorResponseItem>>) -> Unit
    )

    suspend fun getTrainLocationInForgroundService(
        trainId: Int?,
        result: (Resource<LifecycleService>) -> Unit
    )

    suspend fun sendDoctorNotificationToFirebase(
        doctoreNotification: DoctorNotification,
        result: (Resource<String>) -> Unit
    )

    suspend fun getDoctorNotificationFromFirebase(
        userPhone: String,
        result: (Resource<ArrayList<DoctorNotification>>) -> Unit
    )

    suspend fun getUserNotificationInStation(
        stationId: Int,
        result: (Resource<ArrayList<StationUsersNotificationResponseItem>>) -> Unit
    )


    suspend fun getUserInTrain(
        trainid: Int,
        result: (Resource<ArrayList<UserInTrainResponseItem>>) -> Unit
    )

    suspend fun sendUserNotificationTokenToFirebase(
        token: NotificatonToken?,
        result: (Resource<String?>) -> Unit
    )

    suspend fun getNotificationTokenFromFirebase(
        userPhone: String?, result: (Resource<String>) -> Unit
    )

    suspend fun sendDoctorNotificationUsingFCM(
        token: NotificatonToken,
        serverKey: String?,
        doctorNotification: DoctorNotification,
        result: (Resource<String>) -> Unit
    )

    suspend fun subscribeToNewTopic(topicInput: String,result: (Resource<String>) -> Unit)
    suspend fun pushNewTopicNotification(notification: PushNotification, result: (Resource<String>) -> Unit)

    suspend fun getUserCurrantLocationJustOnce(result: (Resource<Location>) -> Unit)
    suspend fun getUserCurrantLocationLive(result: (Resource<Location>) -> Unit)
    suspend fun pushAddPostNotification(notification: PushPostNotification, result: (Resource<String>) -> Unit)
    suspend fun pushAddPostCommentNotification(notification: PushPostCommentNotification, result: (Resource<String>) -> Unit)

    suspend fun createUserNotificationToken(result: (Resource<String>) -> Unit)

    suspend fun getNotificationTokenByUserIDFromApi(userId:Int,result: (Resource<NotificationTokenResponse>) -> Unit)
}