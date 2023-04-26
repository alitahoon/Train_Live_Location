package com.example.trainlivelocation.ui

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(

):ViewModel() {
    var takroffStationTxtClicked = SingleLiveEvent<Boolean>()
    var arrivalStationTxtClicked = SingleLiveEvent<Boolean>()
    var trainIdTxtClicked = SingleLiveEvent<Boolean>()

    fun onTakroffStationTxtClicked(view: View){
        takroffStationTxtClicked.postValue(true)
    }

    fun onArrivalStationTxtClicked(view: View){
        arrivalStationTxtClicked.postValue(true)
    }

    fun onTrainIdTxtClicked(view: View){
        trainIdTxtClicked.postValue(true)
    }
}