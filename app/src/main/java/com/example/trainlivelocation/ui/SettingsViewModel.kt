package com.example.trainlivelocation.ui

import android.app.Activity
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.GetLiveLoctationFromApi
import com.example.domain.usecase.InsertNewStationHistroyItemToDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val insertNewStationHistroyItemToDatabase: InsertNewStationHistroyItemToDatabase,
    private val getLiveLoctationFromApi: GetLiveLoctationFromApi
) :ViewModel(){
    val switchStatestationHistroyAlarms = ObservableBoolean()
    val switchStatePostsNotification = ObservableBoolean()
    val switchStatePostsCommentsNotification = ObservableBoolean()
    val switchStateMessagesNotification = ObservableBoolean()
    val switchStateSharingLocation = ObservableBoolean()


    // Observer for switchState
    fun onSwitchStatestationHistroyAlarms() {
        if (switchStatestationHistroyAlarms.get()) {
            // Switch is ON
            //start history service


        } else {
            // Switch is OFF
            //stop history service


        }
    }

    fun onSwitchStatePostsNotification() {
        if (switchStatePostsNotification.get()) {
            // Switch is ON
        } else {
            // Switch is OFF
        }
    }

    fun switchStatePostsCommentsNotification() {
        if (switchStatePostsCommentsNotification.get()) {
            // Switch is ON
        } else {
            // Switch is OFF
        }
    }

    fun onSwitchStateMessagesNotification() {
        if (switchStateMessagesNotification.get()) {
            // Switch is ON
        } else {
            // Switch is OFF
        }
    }

    fun onSwitchStateSharingLocation() {
        if (switchStateSharingLocation.get()) {
            // Switch is ON
        } else {
            // Switch is OFF
        }
    }



    fun startHistoryService(activiy:Activity){

    }

    fun stopHistoryService(activiy:Activity){

    }
}