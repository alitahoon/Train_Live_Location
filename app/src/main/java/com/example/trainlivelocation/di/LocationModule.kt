package com.example.trainlivelocation.di

import android.content.Context
import com.example.data.LocationLive
import com.example.data.LocationTrackBackgroundService
import com.example.data.LocationTrackForegroundService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    fun ProvideLocationLitener(context: Context): LocationLive {
        return LocationLive(context)
    }
    @Provides
    fun ProvideLocationTrackBackgroundService(): LocationTrackBackgroundService {
        return LocationTrackBackgroundService()
    } @Provides
    fun ProvideLocationTrackForegroundService(): LocationTrackForegroundService {
        return LocationTrackForegroundService()
    }
}