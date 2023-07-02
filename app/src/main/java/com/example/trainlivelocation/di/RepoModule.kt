package com.example.trainlivelocation.di

import android.content.Context
import com.example.data.*
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
    fun ProvideMyDatabase(context: Context): MyDatabase {
        return MyDatabase.getInstance(context)
    }

    @Provides
    fun ProvideGoogleMapsServices(): GoogleMapsServices {
        return GoogleMapsServices()
    }

    @Provides
    fun ProvideRepo(
        apiService: ApiService,
        locationLive: LocationLive,
        locationTrackForegroundService: LocationTrackForegroundService,
        getLocationService: GetLocationService,
        location: userLocation,
        firebaseService: FirebaseService,
        locationServices: LocationServices,
        sharedPreferencesService: SharedPreferencesService,
        GetTrainLocationService: GetTrainLocationService,
        getCurrantLocationJustOnce: GetCurrantLocationJustOnce,
        getCurrantLocationLive: GetCurrantLocationLive,
        myDatabase: MyDatabase,
        googleMapsServices: GoogleMapsServices
    ): UserRepo {
        return userRepoImpl(
            apiService,
            locationLive,
            getLocationService,
            location,
            firebaseService,
            locationServices,
            sharedPreferencesService,
            GetTrainLocationService,
            getCurrantLocationJustOnce,
            getCurrantLocationLive,
            myDatabase,
            googleMapsServices
        )
    }

}