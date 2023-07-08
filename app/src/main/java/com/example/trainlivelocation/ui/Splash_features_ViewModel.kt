package com.example.trainlivelocation.ui

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Splash_features_ViewModel @Inject constructor() : ViewModel() {
    var btnNextClick = SingleLiveEvent <Boolean>()


    fun onBtnNextClick (view: View){
        btnNextClick.value = true
    }

    fun incrementCounter(){
        featuresCounter = featuresCounter.plus(1)
    }
    fun incrementPermissionsCounter(){
        permissionsCounter = permissionsCounter.plus(1)
    }

    companion object{
        var featuresCounter: Int = 1
        var permissionsCounter: Int = 1
    }

    fun  getFeaturesCounter(): Int{
        return featuresCounter
    }
    fun  getPermissionsCounter(): Int{
        return permissionsCounter
    }
}