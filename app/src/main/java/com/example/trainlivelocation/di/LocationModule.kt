package com.example.trainlivelocation.di

import android.content.Context
import com.example.data.*
import com.example.trainlivelocation.utli.LocationTrackBackgroundService
import com.example.trainlivelocation.utli.TrackTrainService
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
    }

    @Provides
    fun ProvideLocationTrackForegroundService(): LocationTrackForegroundService {
        return LocationTrackForegroundService()
    }

    @Provides
    fun ProvideLocationLiveForTracking(context: Context): LocationLiveForTracking {
        return LocationLiveForTracking(context)
    }


    @Provides
    fun ProvideLocationGetLocationService(): GetLocationService {
        return GetLocationService()
    }

    @Provides
    fun ProvideUserLocation(context: Context): userLocation {
        return userLocation(context)
    }

    @Provides
    fun ProvideLocationServices(context: Context): LocationServices {
        return LocationServices(context)
    }

    @Provides
    fun ProvideGetTrainLocationForgroundService(): GetTrainLocationService {
        return GetTrainLocationService()
    }

    @Provides
    fun ProvideTrackTrainService(): TrackTrainService {
        return TrackTrainService()
    }

    @Provides
    fun ProvideGetCurrantLocationJustOnce(context: Context): GetCurrantLocationJustOnce {
        return GetCurrantLocationJustOnce(context)
    }

    @Provides
    fun ProvideGetCurrantLocationLive(context: Context): GetCurrantLocationLive {
        return GetCurrantLocationLive(context)
    }






}