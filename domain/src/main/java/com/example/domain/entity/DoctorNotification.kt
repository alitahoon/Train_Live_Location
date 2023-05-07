package com.example.domain.entity

data class DoctorNotification(
    val content:String,
    val doctorPhone: String,
    val doctorUsername: String,
    val senderPhone:String,
    val senderUserName:String) {
    constructor():this("","","","","")

}