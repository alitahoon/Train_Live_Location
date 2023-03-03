package com.example.trainlivelocation.ui

import SingleLiveEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel

class LocationDialogFragmentViewModel: ViewModel() {
    var trackLocationBtn= SingleLiveEvent<Boolean>()
    var shareLocationBtn= SingleLiveEvent<Boolean>()

    public fun onBtntrackLocationClicked(view: View){
        trackLocationBtn.postValue(true)
    }
    public fun onBtnShareLocationClicked(view: View) {
        shareLocationBtn.postValue(true)
    }

}