package com.example.trainlivelocation.di

import com.example.data.*
import com.example.domain.repo.UserRepo
import com.example.domain.usecase.GetUserLocation
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
        locationTrackBackgroundService: LocationTrackBackgroundService,
        getLocationService: GetLocationService,
        location: userLocation,
        firebaseService: FirebaseService
    ): UserRepo {
        return userRepoImpl(
            apiService,
            locationLive,
            locationTrackBackgroundService,
            getLocationService,
            location,
            firebaseService
        )
    }

}