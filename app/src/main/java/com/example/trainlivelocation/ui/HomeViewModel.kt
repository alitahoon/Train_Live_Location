package com.example.trainlivelocation.ui

import SingleLiveEvent
import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint

class HomeViewModel :ViewModel() {
    var locationBtn= SingleLiveEvent<Boolean>()
    var postsBtn= SingleLiveEvent<Boolean>()
    public fun onLocationBtn(view: View){
        locationBtn.postValue(true)
    }
    public fun onPostsBtn(view: View){
        postsBtn.postValue(true)
    }

}