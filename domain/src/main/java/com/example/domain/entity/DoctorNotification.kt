package com.example.domain.entity

data class DoctorNotification(
    val doctorPhone: String,
    val doctorUsername: String,
    val senderPhone:String,
    val senderUserName:String,
    val senderLocation_Request: Location_Request
) {

}