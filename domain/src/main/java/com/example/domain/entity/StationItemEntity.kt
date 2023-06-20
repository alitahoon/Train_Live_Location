package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StationItemEntity")
data class StationItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "apiID") val apiID: Int,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "nextStation") val nextStation: String,
    @ColumnInfo(name = "nextStation") val Postion: Int,
    @ColumnInfo(name = "trainId") val trainId: Int,

)