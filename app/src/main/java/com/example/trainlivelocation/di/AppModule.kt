package com.example.trainlivelocation.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.trainlivelocation.ui.AuthCheck
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext( application: Application): Context = application.applicationContext






}