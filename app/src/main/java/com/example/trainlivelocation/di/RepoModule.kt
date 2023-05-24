package com.example.trainlivelocation.di

import android.content.Context
import com.example.data.*
import com.example.domain.repo.UserRepo
import com.example.domain.usecase.GetUserCurrantLocationJustOnce
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
        context: Context,
        apiService: ApiService,
        locationLive: LocationLive,
        locationTrackForegroundService: LocationTrackForegroundService,
        locationTrackBackgroundService: LocationTrackBackgroundService,
        getLocationService: GetLocationService,
        location: userLocation,
        firebaseService: FirebaseService,
        locationServices: LocationServices,
        sharedPreferencesService: SharedPreferencesService,
        GetTrainLocationService: GetTrainLocationService,
        getCurrantLocationJustOnce: GetCurrantLocationJustOnce,
        getCurrantLocationLive: GetCurrantLocationLive,
        myDatabase: MyDatabase
    ): UserRepo {
        return userRepoImpl(
            context,
            apiService,
            locationLive,
            locationTrackBackgroundService,
            getLocationService,
            location,
            firebaseService,
            locationServices,
            sharedPreferencesService,
            GetTrainLocationService,
            getCurrantLocationJustOnce,
            getCurrantLocationLive,
            myDatabase
        )
    }

}