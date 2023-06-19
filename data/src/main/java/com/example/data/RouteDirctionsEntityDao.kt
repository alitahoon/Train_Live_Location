package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.RouteDirctionEntity
import com.example.domain.entity.TrainItemEntity
import com.example.domain.entity.UserItemEntity


@Dao
interface RouteDirctionsEntityDao {

    @Query("SELECT * FROM RouteDirctionEntity")
    suspend fun getAllTrainItemEntity() : List<RouteDirctionEntity>

    @Insert
    suspend fun insertTrainItemEntity(routeDirctionEntity: RouteDirctionEntity)

}