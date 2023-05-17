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
        const val API_KEY = "AAAAMRaF_zY:APA91bEue9-CV7da0fKxYfnJoNKn5qEW2wYRq7sVi-ECJbiVIUs_SwFsvKB9PidU32XeLESAa4FnRxgynAT-K_WoautrmAJgcynGc98NOxN_JAKuyp3IRxZX65aytA9Z-3MojjAOehpK"
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