package com.example.trainlivelocation.ui

import com.example.trainlivelocation.utli.SingleLiveEvent
import android.view.View
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