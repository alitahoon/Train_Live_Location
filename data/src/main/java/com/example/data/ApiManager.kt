package com.example.data

import Resource
import com.example.domain.entity.PushNotification

class ApiManager {
    suspend fun postNotification(notification: PushNotification,result: (Resource<String>) -> Unit) {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                result.invoke(Resource.Success("Sending data was successful - notification recipient: ${notification.to}"))
            }else{
                result.invoke(Resource.Success("\"Error sending the data\": ${response.errorBody()}"))
            }
        } catch (e: Exception) {
            result.invoke(Resource.Success("${e.message}"))
        }
    }
}