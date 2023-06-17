package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.StationHistoryAlarmEntity
import com.example.domain.entity.TicketItemEntity

@Dao
interface StationHistoryAlarmDao {

    @Query("SELECT * FROM stationHistoryAlarm")
    suspend fun getStationHistoryAlarmEntity() : List<StationHistoryAlarmEntity>

    @Insert
    suspend fun insertStationHistoryAlarmEntity(stationHistoryAlarmEntity: StationHistoryAlarmEntity)
}