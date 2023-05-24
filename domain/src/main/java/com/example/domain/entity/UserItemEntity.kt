package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "UserItemEntity")
data class UserItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "address") val address: String,
    val birthDate: String,
    val email: String,
    val gender: String,
    val apiId: Int,
    val jop: String,
    val name: String,
    val password: String,
    val phone: String,
    val role: String,
    val tokenForNotifications:String,
    @ColumnInfo (name ="longitude ") val longitude:Double,
    @ColumnInfo (name ="latitude ") val latitude:Double
)