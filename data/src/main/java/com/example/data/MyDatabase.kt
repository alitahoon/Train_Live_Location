package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.domain.entity.StationAlarmEntity
import com.example.domain.entity.UserItemEntity

@Database(entities = [StationAlarmEntity::class, UserItemEntity::class], version = 1)
abstract class MyDatabase(private val context: Context) : RoomDatabase() {
    init {
        getInstance(context)
    }

    abstract fun stationAlarmDao(): StationAlarmDao
    abstract fun UserItemEntityDao(): UserItemEntityDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): RoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "MY_DATABASE"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
