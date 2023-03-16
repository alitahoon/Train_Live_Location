package com.example.trainlivelocation.ui

import com.example.trainlivelocation.utli.SingleLiveEvent
import android.view.View
import androidx.lifecycle.ViewModel

class HomeViewModel :ViewModel() {
    var locationBtn= SingleLiveEvent<Boolean>()
    var postsBtn= SingleLiveEvent<Boolean>()
    var locationCardBtn= SingleLiveEvent<Boolean>()
    public fun onLocationBtn(view: View){
        locationBtn.postValue(true)
    }
    public fun onPostsBtn(view: View){
        postsBtn.postValue(true)
    }
    public fun onLocationCardBtn(view:View){
        locationCardBtn.postValue(true)
    }


}