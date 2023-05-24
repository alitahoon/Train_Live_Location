package com.example.trainlivelocation.di

import android.content.Context
import androidx.room.RoomDatabase
import com.example.data.*
import com.example.domain.repo.UserRepo
import com.example.domain.usecase.GetUserCurrantLocationJustOnce
import com.example.repo.userRepoImpl
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun ProvideFirebaseStorage(context: Context): MyDatabase {
        return MyDatabase.getInstance(context) as MyDatabase
    }

    @Provides
    fun ProvideRepo(
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