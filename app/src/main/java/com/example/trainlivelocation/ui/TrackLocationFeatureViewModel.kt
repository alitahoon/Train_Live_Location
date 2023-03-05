package com.example.trainlivelocation.ui

import SingleLiveEvent
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Location_Response
import com.example.domain.usecase.GetLiveLoctationFromApi
import com.example.domain.usecase.GetLocationTrackBackgroundService
import com.example.domain.usecase.GetLocationTrackForegroundService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackLocationFeatureViewModel @Inject constructor(
    private val getLiveLoctationFromApi: GetLiveLoctationFromApi,
    private var getLocationTrackForegroundService: GetLocationTrackForegroundService
) :ViewModel(){
    private var TAG:String?="TrackLocationFeatureViewModel"
    lateinit var trackLocationListener:TrackLocationListener
    val _trainLocationMuta: MutableLiveData<Location_Response?> =
        MutableLiveData(null)
    var btnTrackLocationFeature=SingleLiveEvent<Boolean>()
    var trainid:String?=null
    private lateinit var activity: Activity
    var locationForegrondservice: Intent? = null

    fun setbaseActivity(baseActivity: Activity) {
        activity = baseActivity
    }
    public fun onBtnTrackTrainLocationClicked(view: View){
        btnTrackLocationFeature.postValue(true)
    }
    fun setLocationForgroundServices() {
        viewModelScope.launch {
            Log.e(TAG,"setLocationForgroundServices")
            getLocationTrackForegroundService(trainid?.toInt())
            locationForegrondservice =
                Intent(activity, getLocationTrackForegroundService::class.java)
        }
    }

    public fun getTrainLocationFromApi(){
        viewModelScope.launch {
           var result=getLiveLoctationFromApi(trainid!!.toInt())
            if (result.isSuccessful){
                if (result.body()!=null){
                    _trainLocationMuta.postValue(result.body())
                }else{
                    Log.e("TAG",result.errorBody().toString())
                }
            }else{
                Log.e(TAG,result.message())
            }
        }
    }
    fun startTrackLocationForgroundService(){
        Log.e(TAG,"startTrackLocationForgroundService")
        setLocationForgroundServices()
        activity.startService(locationForegrondservice)
    }
    fun stopTrackLocationForgroundService(){
        activity.stopService(locationForegrondservice)
    }
}