package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.UserSignInDataEntity


@Dao
interface UserSignInDataEntityDao {

    @Query("SELECT * FROM UserSignInDataEntity")
    suspend fun getAllUserSignInDataEntity() : List<UserSignInDataEntity>

    @Insert
    suspend fun insertUserSignInDataEntity(userSignInDataEntity: UserSignInDataEntity)

    @Query("DELETE FROM UserSignInDataEntity")
    fun clear()
}