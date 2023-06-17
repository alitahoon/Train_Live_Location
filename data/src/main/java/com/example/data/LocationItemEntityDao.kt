package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.domain.entity.LocationItemEntity
import com.example.domain.entity.StationAlarmEntity

@Dao
interface LocationItemEntityDao {

    @Insert
    suspend fun insertLocationItemEntity(locationItemEntity: LocationItemEntity)

    @Update
    suspend fun updateLocationItemEntity(locationItemEntity: LocationItemEntity)


    @Query("DELETE FROM LocationItemEntity WHERE id = :itemId")
    suspend fun deleteLocationItemEntity(itemId: Long)

}