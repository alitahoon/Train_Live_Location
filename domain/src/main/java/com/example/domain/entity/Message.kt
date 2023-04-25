package com.example.domain.entity

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message")
    val message: String?,
    @SerializedName("senderUserName")
    val senderUserName: String?,
    @SerializedName("recieverUserName")
    val recieverUserName: String?,
    @SerializedName("sender")
    val sender: String?,
    @SerializedName("reciever")
    val reciever: String?,
    @SerializedName("title")
    val title: String?
){
    constructor():this("","","","","","")
}
