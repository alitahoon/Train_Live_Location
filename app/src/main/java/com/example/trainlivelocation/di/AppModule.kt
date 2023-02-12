package com.example.trainlivelocation.di

import android.app.Application
import com.example.domain.repo.UserRepo
import com.example.domain.usecase.AddNewUser
import com.example.trainlivelocation.R
import com.example.trainlivelocation.ui.PhoneCallbacksListener
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesArrayString(@ApplicationContext context: Application):Application{
        return context
    }
}