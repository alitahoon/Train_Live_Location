package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageItemEntityDao {

    @Query("SELECT * FROM MessageItemEntity")
    suspend fun getAllMessageItemEntity() : List<MessageItemEntity>

    @Insert
    suspend fun insertMessageItemEntity(messageItemEntity: MessageItemEntity)

}