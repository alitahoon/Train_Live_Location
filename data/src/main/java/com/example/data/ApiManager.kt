package com.example.data

import com.example.domain.entity.PushNotification

class ApiManager {
    suspend fun postNotification(notification: PushNotification) {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                println("Sending data was successful - notification recipient: ${notification.to}")
            }else{
                println("Error sending the data")
            }
        } catch (e: Exception) {
            println(e.message.toString())
        }
    }
}