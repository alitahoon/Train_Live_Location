package com.example.trainlivelocation.ui

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Splash_features_ViewModel @Inject constructor() : ViewModel() {
    var btnNextClick = SingleLiveEvent <Boolean>()
    var featuresCounter: Int = 1

    fun onBtnNextClick (view: View){
        btnNextClick.value = true
    }

    fun incrementCounter(){
        featuresCounter.plus(1)
    }
}