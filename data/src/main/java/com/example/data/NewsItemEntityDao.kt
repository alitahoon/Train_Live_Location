package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.NewsItemEntity


@Dao
interface NewsItemEntityDao {

    @Query("SELECT * FROM NewsItemEntity")
    suspend fun getAllNewsItemEntity() : List<NewsItemEntity>

    @Insert
    suspend fun insertNewsItemEntity(newsItemEntity: NewsItemEntity)

}