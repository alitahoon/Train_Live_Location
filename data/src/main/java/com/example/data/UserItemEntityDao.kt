package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.UserItemEntity


@Dao
interface UserItemEntityDao {

    @Query("SELECT * FROM UserItemEntity")
    suspend fun getAllUserItemEntity() : ArrayList<UserItemEntity>

    @Insert
    suspend fun insertUserItemEntity(userItemEntity: UserItemEntity)
}