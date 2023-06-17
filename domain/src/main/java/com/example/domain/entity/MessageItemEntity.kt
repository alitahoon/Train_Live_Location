package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "MessageItemEntity")
data class MessageItemEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name ="message") val message: String?,
    @ColumnInfo(name ="senderUserName") val senderUserName: String?,
    @ColumnInfo(name ="recieverUserName") val recieverUserName: String?,
    @ColumnInfo(name ="senderPhone") val senderPhone: String?,
    @ColumnInfo(name ="recieverPhone") val recieverPhone: String?,
    @ColumnInfo(name ="title") val title: String?
)