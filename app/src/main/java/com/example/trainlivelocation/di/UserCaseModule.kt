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
    fun provideSendProfileImageToFirebaseStorage(userRepo: UserRepo):SendImageToFirebaseStorage{
        return SendImageToFirebaseStorage(userRepo)
    }
    @Provides
    fun provideGetLocationLive(userRepo: UserRepo):GetUserLocationLive{
        return GetUserLocationLive(userRepo)
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
    fun provideGetUserLocation(userRepo: UserRepo) :GetUserLocation{
        return GetUserLocation(userRepo)
    }


    @Provides
    fun provideCreatePost(userRepo: UserRepo) :CreatePost{
        return CreatePost(userRepo)
    }
    @Provides
    fun provideGetAllPostsFromAPI(userRepo: UserRepo) :GetAllPostsFromAPI{
        return GetAllPostsFromAPI(userRepo)
    }
    @Provides
    fun provideStopLocationUpdate(userRepo: UserRepo) :StopLocationUpdate{
        return StopLocationUpdate(userRepo)
    }
    @Provides
    fun provideGetUserDataById(userRepo: UserRepo) :GetUserDataById{
        return GetUserDataById(userRepo)
    }

    @Provides
    fun provideCreateAPhoneAuthCredential(userRepo: UserRepo) :CreateAPhoneAuthCredential{
        return CreateAPhoneAuthCredential(userRepo)
    }

    @Provides
    fun provideSetFirebaseServiceActivity(userRepo: UserRepo) :SetFirebaseServiceActivity{
        return SetFirebaseServiceActivity(userRepo)
    }

    @Provides
    fun provideGetAllStations(userRepo: UserRepo) :GetAllStations{
        return GetAllStations(userRepo)
    }

    @Provides
    fun provideGetImageFromFirebaseStorage(userRepo: UserRepo) :GetImageFromFirebaseStorage{
        return GetImageFromFirebaseStorage(userRepo)
    }


    @Provides
    fun provideGetAllTrains(userRepo: UserRepo) :GetAllTrains{
        return GetAllTrains(userRepo)
    }

    @Provides
    fun providCreatePostComment(userRepo: UserRepo) :CreatePostComment{
        return CreatePostComment(userRepo)
    }
    @Provides
    fun providGetCommentsForPostUsingId(userRepo: UserRepo) :GetCommentsForPostUsingId{
        return GetCommentsForPostUsingId(userRepo)
    }

    @Provides
    fun providDeletePostWithID(userRepo: UserRepo) :DeletePostWithID{
        return DeletePostWithID(userRepo)
    }

    @Provides
    fun providGetStationById(userRepo: UserRepo) :GetStationById{
        return GetStationById(userRepo)
    }

    @Provides
    fun providUpdateUserData(userRepo: UserRepo) :UpdateUserData{
        return UpdateUserData(userRepo)
    }

    @Provides
    fun providSendMessageToFirebasechat(userRepo: UserRepo) :SendMessageToFirebasechat{
        return SendMessageToFirebasechat(userRepo)
    }

    @Provides
    fun providGetChatFromFirebase(userRepo: UserRepo) :GetChatFromFirebase{
        return GetChatFromFirebase(userRepo)
    }


    @Provides
    fun providGetInboxRecieveChatFromFirebase(userRepo: UserRepo) :GetInboxRecieveChatFromFirebase{
        return GetInboxRecieveChatFromFirebase(userRepo)
    }

    @Provides
    fun providGetInboxSentChatFromFirebase(userRepo: UserRepo) :GetInboxSentChatFromFirebase{
        return GetInboxSentChatFromFirebase(userRepo)
    }

    @Provides
    fun providGetDataFromSharedPrefrences(userRepo: UserRepo) :GetDataFromSharedPrefrences{
        return GetDataFromSharedPrefrences(userRepo)
    }


    @Provides
    fun providCreateTicket(userRepo: UserRepo) :CreateTicket{
        return CreateTicket(userRepo)
    }

    @Provides
    fun providGetDoctorInTrain(userRepo: UserRepo) :GetDoctorInTrain{
        return GetDoctorInTrain(userRepo)
    }

    @Provides
    fun ProvideGetTrainLocationInForgroundService(userRepo: UserRepo): GetTrainLocationInForgroundService {
        return GetTrainLocationInForgroundService(userRepo)
    }
    @Provides
    fun ProvideGetSendDoctorNotificationToFirebase(userRepo: UserRepo): SendDoctorNotificationToFirebase {
        return SendDoctorNotificationToFirebase(userRepo)
    }
    @Provides
    fun ProvideGetDoctorNotificationFromFirebase(userRepo: UserRepo): GetDoctorNotificationFromFirebase {
        return GetDoctorNotificationFromFirebase(userRepo)
    }

    @Provides
    fun ProvideGetUserNotificationInStation(userRepo: UserRepo): GetUserNotificationInStation {
        return GetUserNotificationInStation(userRepo)
    }

    @Provides
    fun ProvideGetUserInTrain(userRepo: UserRepo): GetUserInTrain {
        return GetUserInTrain(userRepo)
    }
    @Provides
    fun ProvideSendUserNotificationTokenToFirebase(userRepo: UserRepo): SendUserNotificationTokenToFirebase {
        return SendUserNotificationTokenToFirebase(userRepo)
    }

    @Provides
    fun ProvideGetNotificationTokenFromFirebase(userRepo: UserRepo): GetNotificationTokenFromFirebase {
        return GetNotificationTokenFromFirebase(userRepo)
    }

    @Provides
    fun ProvideSendDoctorNotificationUsingFCM(userRepo: UserRepo): SendDoctorNotificationUsingFCM {
        return SendDoctorNotificationUsingFCM(userRepo)
    }

    @Provides
    fun ProvideSubscribeToNewTopic(userRepo: UserRepo): SubscribeToNewTopic {
        return SubscribeToNewTopic(userRepo)
    }

    @Provides
    fun ProvidePushNewTopicNotification(userRepo: UserRepo): PushNewTopicNotification {
        return PushNewTopicNotification(userRepo)
    }

    @Provides
    fun ProvideGetUserCurrantLocationJustOnce(userRepo: UserRepo): GetUserCurrantLocationJustOnce {
        return GetUserCurrantLocationJustOnce(userRepo)
    }





}
