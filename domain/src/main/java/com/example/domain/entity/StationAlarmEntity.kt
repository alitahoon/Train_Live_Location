package com.example.domain.entity


@Entity(tableName = "stationAlarm")
data class StationAlarmEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "apiID") val apiId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "distance") val distance: Int,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "latitude") val latitude: Double
        )

data class StationAlarmEntity(
    val alarmName:String,
    val distance: Int,
    val longitude: Double,
    val latitude: Double
)
