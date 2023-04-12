package com.example.domain.entity

data class TrainResponseItem(
    val conductor: String,
    val currentLocation: String,
    val degree: String,
    val driver: String,
    val id: Int,
    val numOfSeat: String,
    val numOfTrainCars: String,
    val trainNumber: String
)