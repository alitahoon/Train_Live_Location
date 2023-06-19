package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RouteDirctionEntity")
data class RouteDirctionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "polyline") val polyline: String,
    @ColumnInfo(name = "distance") val distance: Double,
    @ColumnInfo(name = "duration") val duration: Double
)