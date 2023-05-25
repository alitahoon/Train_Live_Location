package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "TicketItemEntity")
data class TicketItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "arrivalStation") val arrivalStation: String,
    @ColumnInfo(name = "apiId") val apiId: Int,
    @ColumnInfo(name = "numOfSeat") val numOfSeat: Int,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "takeOffDate") val takeOffDate: String,
    @ColumnInfo(name = "takeOffStation") val takeOffStation: String,
    @ColumnInfo(name = "trainDegree") val trainDegree: String,
    @ColumnInfo(name = "trainId") val trainId: Int,
    @ColumnInfo(name = "trainNumber") val trainNumber: String,
    @ColumnInfo(name = "userId") val userId: Int
)