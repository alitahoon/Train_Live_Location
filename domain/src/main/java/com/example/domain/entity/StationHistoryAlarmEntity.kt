package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stationHistoryAlarm")
data class StationHistoryAlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "distance") val distance: Double,
    @ColumnInfo(name = "duration") val duration: Double,
    @ColumnInfo(name = "stationName") val stationName: String,
    @ColumnInfo(name = "discription") val discription: String
)