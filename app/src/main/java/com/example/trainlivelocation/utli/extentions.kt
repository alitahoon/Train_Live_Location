package com.example.trainlivelocation.utli


import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.example.domain.entity.UserResponseItem


//fun Context.setCurrentTopic(token: String,){
//    val sharedPreferences: SharedPreferences =
//        getSharedPreferences(TOPIC_PREFS, Context.MODE_PRIVATE)
//    sharedPreferences.edit().putString(SUBSCRIBED_TOPIC, token).apply()
//}
//fun Fragment.setCurrentTopic(context: Context,token: String){
//    val sharedPreferences: SharedPreferences =
//        context.getSharedPreferences(TOPIC_PREFS, Context.MODE_PRIVATE)
//    sharedPreferences.edit().putString(SUBSCRIBED_TOPIC, token).apply()
//}

fun Fragment.getuserModelFromSharedPreferences(context: Context): UserResponseItem{
    val userSharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserToken", Context.MODE_PRIVATE)
    return   UserResponseItem(
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
        userSharedPreferences.getString("tokenForNotifications","empty")!!
    )
}

fun Context.getuserModelFromSharedPreferences(): UserResponseItem{
    val userSharedPreferences: SharedPreferences =
        getSharedPreferences("UserToken", Context.MODE_PRIVATE)
    return   UserResponseItem(
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
        userSharedPreferences.getString("tokenForNotifications","empty")!!

    )
}