package com.example.trainlivelocation.di

import android.content.Context
import com.example.data.*
import com.example.domain.usecase.GetTrainLocationInForgroundService
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
    fun ProvideLocationTrackBackgroundService(apiService: ApiService): LocationTrackBackgroundService {
        return LocationTrackBackgroundService(apiService)
    }
    @Provides
    fun ProvideLocationTrackForegroundService(): LocationTrackForegroundService {
        return LocationTrackForegroundService()
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
    fun ProvideGetTrainLocationForgroundService(apiService:ApiService,context: Context): getTrainLocationForgroundService {
        return getTrainLocationForgroundService(apiService,context)
    }


}