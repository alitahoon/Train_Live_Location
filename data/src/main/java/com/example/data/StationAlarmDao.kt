package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.StationAlarmEntity

@Dao
interface StationAlarmDao {
    @Query("SELECT * FROM stationAlarm")
    suspend fun getAllStationAlarmEntity(): ArrayList<StationAlarmEntity>

    @Insert
    suspend fun insertStationAlarmEntity(stationAlarmEntity: StationAlarmEntity)
}