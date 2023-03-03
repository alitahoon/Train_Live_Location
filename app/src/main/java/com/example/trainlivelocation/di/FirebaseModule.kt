//package com.example.trainlivelocation.di
//
//import android.content.Context
//import com.example.data.LocationLive
//import com.example.data.LocationTrackForegroundService
//import com.example.domain.repo.LocationListener
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//
//@Module
//@InstallIn(SingletonComponent::class)
//object FirebaseModule {
//
//    @Provides
//    fun ProvideCallbackInterface(): LocationLive {
//
//    }
//    @Provides
//    fun ProvideLocationTrackBackgroundService(): LocationTrackForegroundService {
//        return LocationTrackForegroundService()
//    }
//}