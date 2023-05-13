package com.example.trainlivelocation.utli


import android.content.Context
import android.content.SharedPreferences

const val TOPIC_PREFS = "topic_storage_pref"
const val SUBSCRIBED_TOPIC = "subscribed_topic"

fun Context.setCurrentTopic(token: String){
    val sharedPreferences: SharedPreferences =
        getSharedPreferences(TOPIC_PREFS, Context.MODE_PRIVATE)
    sharedPreferences.edit().putString(SUBSCRIBED_TOPIC, token).apply()
}

fun Context.getCurrentTopic(): String{
    val sharedPreferences: SharedPreferences =
        getSharedPreferences(TOPIC_PREFS, Context.MODE_PRIVATE)
    //TODO explain that the initial value should be a valid topic name (no spaces no special characters)
    return  sharedPreferences.getString(SUBSCRIBED_TOPIC, "EmptySharedPref") ?: "Error finding Topic"
}