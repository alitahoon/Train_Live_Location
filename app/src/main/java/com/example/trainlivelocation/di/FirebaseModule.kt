package com.example.trainlivelocation.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.example.data.ApiManager
import com.example.data.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun ProvideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    fun ProvideStorageReference(firebaseStorage: FirebaseStorage): StorageReference {
        return firebaseStorage.getReferenceFromUrl("gs://trainlivelocation-4df97.appspot.com/")
    }
    @Provides
    fun ProvideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun ProvideFirebaseDatabse(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    fun ProvideDatabaseReference(database: FirebaseDatabase): DatabaseReference {
        return database.getReference()
    }

    @Provides
    fun ProvideFirebaseService(
        firebaseAuth: FirebaseAuth,
        storageReference: StorageReference,
        databaseReference: DatabaseReference,
        firebaseMessaging: FirebaseMessaging
    ): FirebaseService {
        return FirebaseService(firebaseAuth,storageReference,databaseReference,firebaseMessaging)
    }

    @Provides
    fun ProvideFirebaseMessaging(
    ): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }

    @Provides
    fun ProvideFirebaseMessaging(
        apiManager: ApiManager
    ): ApiManager {
        return apiManager
    }


}