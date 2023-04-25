package com.example.data

import Resource
import android.content.Context
import android.content.SharedPreferences
import com.example.domain.entity.UserResponseItem

class SharedPreferencesService(private val context: Context) {
    fun getUserData(
        sharedPrefFile: String?,
        result: (Resource<UserResponseItem>) -> Unit
    ) {
        try {
            val userSharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
            result.invoke(Resource.Success(  UserResponseItem(
                userSharedPreferences.getString("userAddress","empty")!!,
                userSharedPreferences.getString("userBirthdate","empty")!!,
                userSharedPreferences.getString("userEmail","empty")!!,
                userSharedPreferences.getString("userGender","empty")!!,
                userSharedPreferences.getInt("userId",0),
                userSharedPreferences.getString("userJop","empty")!!,
                userSharedPreferences.getString("userName","empty")!!,
                userSharedPreferences.getString("userPassword","empty")!!,
                userSharedPreferences.getString("userPhone","empty")!!,
                userSharedPreferences.getString("userRole","empty")!!,
                userSharedPreferences.getInt("userStationId",0)
            )))
        }catch (e:Exception){
            result.invoke(Resource.Failure("${e.message}"))
        }

    }
}