package com.example.data

import com.example.domain.entity.PushNotification
import com.example.domain.entity.PushPostCommentNotification
import com.example.domain.entity.PushPostNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface NotificationApi {
    companion object{
        const val CONTENT_TYPE = "application/json"
        const val API_KEY = "AAAAZGOmP6o:APA91bEUkTb5AucaPfa7V32J5FpUUkeHR-uYAv7rIXIAFBOgfk2HEz6A2tqXRE8EJ6S_eCEIZXiIZ1DGZ_tUOtRaIYMiwslkCBOM80cryStRzMVEEstG2BcPR-cVo44KNirAQRX0ZR5g"
    }

    @Headers("Authorization: key=${API_KEY}", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>

    @Headers("Authorization: key=${API_KEY}", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postAddedPostNotification(
        @Body notification: PushPostNotification
    ): Response<ResponseBody>


    @Headers("Authorization: key=${API_KEY}", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postAddedPostCommentNotification(
        @Body notification: PushPostCommentNotification
    ): Response<ResponseBody>
}