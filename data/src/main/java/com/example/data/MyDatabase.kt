package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.domain.entity.*

@Database(entities = [StationAlarmEntity::class, UserItemEntity::class, TrainItemEntity::class, TicketItemEntity::class, MessageItemEntity::class], version = 1)
abstract class MyDatabase: RoomDatabase() {

    abstract fun stationAlarmDao(): StationAlarmDao
    abstract fun UserItemEntityDao(): UserItemEntityDao

    abstract fun TrainItemEntityDao(): TrainItemEntityDao

    abstract fun TicketItemEntityDao(): TicketItemEntityDao

    abstract fun MessageItemEntityDao(): MessageItemEntityDao

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
