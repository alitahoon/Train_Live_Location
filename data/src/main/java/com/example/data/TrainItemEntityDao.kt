package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.TrainItemEntity
import com.example.domain.entity.UserItemEntity


@Dao
interface TrainItemEntityDao {

    @Query("SELECT * FROM TrainItemEntity")
    suspend fun getAllTrainItemEntity() : List<TrainItemEntity>

    @Insert
    suspend fun insertTrainItemEntity(trainItemEntity: TrainItemEntity)

}