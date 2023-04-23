package com.example.domain.entity

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message")
    val message: String?,
    @SerializedName("sender")
    val sender: String?,
    @SerializedName("reciever")
    val reciever: String?,
    @SerializedName("title")
    val title: String?
){
    constructor():this("","","","")
}
