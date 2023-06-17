package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "TrainItemEntity")
data class TrainItemEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name ="conductor ") val conductor: String,
    @ColumnInfo(name ="currentLocation ") val currentLocation: String,
    @ColumnInfo(name ="degree ") val degree: String,
    @ColumnInfo(name ="driver ") val driver: String,
    @ColumnInfo(name ="apiId ") val apiId: Int,
    @ColumnInfo(name ="numOfSeat ") val numOfSeat: String,
    @ColumnInfo(name ="numOfTrainCars ") val numOfTrainCars: String,
    @ColumnInfo(name ="trainNumber ") val trainNumber: String,
    @ColumnInfo(name ="longitude ") val longitude:Double,
    @ColumnInfo(name ="latitude ") val latitude:Double

)
