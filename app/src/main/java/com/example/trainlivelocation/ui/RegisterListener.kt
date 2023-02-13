package com.example.trainlivelocation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import kotlinx.coroutines.flow.StateFlow

interface RegisterListener {
    fun onStartRegister()
    fun onSuccessRegister()
    fun onOtbCodeSendToUser()

    fun onFailure(message:String)
}