package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "NewsItemEntity")
data class NewsItemEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "contentOfPost") val contentOfPost: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "apiId") val apiId: Int,
    @ColumnInfo(name = "img") val img: String,
    @ColumnInfo(name = "title") val title:String
    )