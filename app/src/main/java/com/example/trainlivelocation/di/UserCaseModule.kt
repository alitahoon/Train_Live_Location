package com.example.trainlivelocation.di

import com.example.domain.repo.UserRepo
import com.example.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserCaseModule  {

    @Provides
    fun provideUserGetCaseUserData(userRepo: UserRepo): GetUserData{
        return GetUserData(userRepo)
    }
    @Provides
    fun provideAddNewUser(userRepo: UserRepo):AddNewUser {
        return AddNewUser(userRepo)
    }

    @Provides
    fun provideSendOtbToPhone(userRepo: UserRepo):SendOtpToPhone{
        return SendOtpToPhone(userRepo)
    }
    @Provides
    fun provideReSendOtbToPhone(userRepo: UserRepo):ResendOtpCode{
        return ResendOtpCode(userRepo)
    }

    @Provides
    fun provideSignInWithPhoneAuthCredential(userRepo: UserRepo):SignInWithPhoneAuthCredential{
        return SignInWithPhoneAuthCredential(userRepo)
    }
    @Provides
    fun provideSendProfileImageToFirebaseStorage(userRepo: UserRepo):SendProfileImageToFirebaseStorage{
        return SendProfileImageToFirebaseStorage(userRepo)
    }
    @Provides
    fun provideGetLocationLive(userRepo: UserRepo):GetLocationLive{
        return GetLocationLive(userRepo)
    }

    @Provides
    fun provideStartLocationUpdate(userRepo: UserRepo):StartLocationUpdate{
        return StartLocationUpdate(userRepo)
    }

    @Provides
    fun provideGetLocationTrackBackgroundService(userRepo: UserRepo): GetLocationTrackBackgroundService {
        return GetLocationTrackBackgroundService(userRepo)
    }
    @Provides
    fun provideGetLocationTrackForegroundService(userRepo: UserRepo): GetLocationTrackForegroundService {
        return GetLocationTrackForegroundService(userRepo)
    }

    @Provides
    fun provideGetLiveLoctationFromApi(userRepo: UserRepo): GetLiveLoctationFromApi {
        return GetLiveLoctationFromApi(userRepo)
    }


    @Provides
    fun provideAddLiveLoctationToApi(userRepo: UserRepo) :AddLiveLoctationToApi{
        return AddLiveLoctationToApi(userRepo)
    }

    @Provides
    fun provideCreatePost(userRepo: UserRepo) :CreatePost{
        return CreatePost(userRepo)
    }



}
