package com.example.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class userResponseItem(
    val address: String,
    val birthDate: String,
    val email: String,
    val gender: String,
    val id: Int,
    val jop: String,
    val name: String,
    val password: String,
    val phone: String,
    val role: String,
    val stationId:Int
) : Parcelable