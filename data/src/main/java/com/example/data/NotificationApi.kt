package com.example.data

import com.example.domain.entity.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface NotificationApi {
    companion object{
        const val CONTENT_TYPE = "application/json"
        const val API_KEY = "ENTER YOUR API KEY"
    }

    @Headers("Authorization: key=${API_KEY}", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}