package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.StationHistoryAlarmEntity
import com.example.domain.entity.StationItemEntity
import com.example.domain.entity.TicketItemEntity

@Dao
interface StationItemEntityDao {

    @Query("SELECT * FROM StationItemEntity")
    suspend fun getStationItemEntityDao() : List<StationItemEntity>

    @Insert
    suspend fun insertStationItemEntityDao(stationItemEntity: StationItemEntity)
    @Query("DELETE FROM StationItemEntity")
    fun clear()
}