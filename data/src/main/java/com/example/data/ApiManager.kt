package com.example.data

import Resource
import com.example.domain.entity.PushMessageNotification
import com.example.domain.entity.PushNotification
import com.example.domain.entity.PushPostCommentNotification
import com.example.domain.entity.PushPostNotification

class ApiManager {
    suspend fun postNotification(notification: PushNotification, result: (Resource<String>) -> Unit) {
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
    suspend fun postAddedNotification(notification: PushPostNotification, result: (Resource<String>) -> Unit) {
        try {
            val response = RetrofitInstance.api.postAddedPostNotification(notification)
            if (response.isSuccessful){
                result.invoke(Resource.Success("Sending data was successful - notification recipient: ${notification.to}"))
            }else{
                result.invoke(Resource.Success("\"Error sending the data\": ${response.errorBody()}"))
            }
        } catch (e: Exception) {
            result.invoke(Resource.Success("${e.message}"))
        }
    }

    suspend fun postCommentAddedNotification(notification: PushPostCommentNotification, result: (Resource<String>) -> Unit) {
        try {
            val response = RetrofitInstance.api.postAddedPostCommentNotification(notification)
            if (response.isSuccessful){
                result.invoke(Resource.Success("Sending data was successful - notification recipient: ${notification.to}"))
            }else{
                result.invoke(Resource.Success("\"Error sending the data\": ${response.errorBody()}"))
            }
        } catch (e: Exception) {
            result.invoke(Resource.Success("${e.message}"))
        }
    }
    suspend fun MessageNotification(notification: PushMessageNotification, result: (Resource<String>) -> Unit) {
        try {
            val response = RetrofitInstance.api.MessageNotification(notification)
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