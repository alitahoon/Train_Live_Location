package com.example.trainlivelocation.di

import com.example.data.ApiService
import com.example.data.LocationLive
import com.example.data.LocationTrackBackgroundService
import com.example.data.LocationTrackForegroundService
import com.example.domain.repo.UserRepo
import com.example.repo.userRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    fun ProvideRepo(
        apiService: ApiService,
        locationLive: LocationLive,
        locationTrackForegroundService: LocationTrackForegroundService,
        locationTrackBackgroundService: LocationTrackBackgroundService
    ): UserRepo {
        return userRepoImpl(
            apiService,
            locationLive,
            locationTrackForegroundService,
            locationTrackBackgroundService
        )
    }

}