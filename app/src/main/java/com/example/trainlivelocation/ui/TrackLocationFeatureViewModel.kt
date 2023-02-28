package com.example.trainlivelocation.ui

import SingleLiveEvent
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Location_Response
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.GetLiveLoctationFromApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackLocationFeatureViewModel @Inject constructor(
    private val getLiveLoctationFromApi: GetLiveLoctationFromApi
) :ViewModel(){
    private var TAG:String?="TrackLocationFeatureViewModel"
    lateinit var trackLocationListener:TrackLocationListener
    val _trainLocationMuta: MutableLiveData<Location_Response?> =
        MutableLiveData(null)
    var btnTrackLocationFeature=SingleLiveEvent<Boolean>()
    var trainid:String?=null

    public fun onBtnTrackTrainLocationClicked(view: View){
        Log.e(TAG,"hhhhhhhhhhhh")
        trackLocationListener.onBtnClickListener()
        btnTrackLocationFeature.postValue(true)
    }

    public fun getTrainLocationFromApi(){
        viewModelScope.launch {
           var result=getLiveLoctationFromApi(trainid!!.toInt())
            if (result.isSuccessful){
                if (result.body()==null){
                    Log.e(TAG,"nulllllllllllll")
                }
                _trainLocationMuta.postValue(result.body())
            }else{
                Log.e(TAG,result.message())
            }
        }
    }
}