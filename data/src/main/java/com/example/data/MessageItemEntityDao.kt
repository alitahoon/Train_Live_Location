package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.MessageItemEntity

@Dao
interface MessageItemEntityDao {

    @Query("SELECT * FROM MessageItemEntity")
    suspend fun getAllMessageItemEntity() : ArrayList<MessageItemEntity>

    @Insert
    suspend fun insertMessageItemEntity(messageItemEntity: MessageItemEntity)

}