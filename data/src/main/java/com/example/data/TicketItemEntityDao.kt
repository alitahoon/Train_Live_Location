package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.TicketItemEntity
import com.example.domain.entity.TrainItemEntity


@Dao
interface TicketItemEntityDao {

    @Query("SELECT * FROM TicketItemEntity")
    suspend fun getAllTicketItemEntity() : ArrayList<TicketItemEntity>

    @Insert
    suspend fun insertTicketItemEntity(ticketItemEntity: TicketItemEntity)

}