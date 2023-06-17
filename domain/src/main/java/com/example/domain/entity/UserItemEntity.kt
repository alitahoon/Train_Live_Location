package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "UserItemEntity")
data class UserItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "birthDate") val birthDate: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "apiId") val apiId: Int,
    @ColumnInfo(name = "jop") val jop: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "role") val role: String,
    @ColumnInfo(name = "tokenForNotifications") val tokenForNotifications:String,
    @ColumnInfo (name ="longitude ") val longitude:Double,
    @ColumnInfo (name ="latitude ") val latitude:Double
)