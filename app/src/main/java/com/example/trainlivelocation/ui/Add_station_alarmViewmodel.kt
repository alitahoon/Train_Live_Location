package com.example.trainlivelocation.ui

import Resource
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.CommentResponse
import com.example.domain.entity.StationAlarmEntity
import com.example.domain.usecase.InsertNewStationAlarm
import com.example.trainlivelocation.utli.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class Add_station_alarmViewmodel @Inject constructor(
    private val insertNewStationAlarm: InsertNewStationAlarm
) : ViewModel() {
    var btnAddStationAlarmClicked = SingleLiveEvent<Boolean>()
    var btnChooseStationClicked = SingleLiveEvent<Boolean>()
    var userStation:String? = null


    private val _insertStationAlarm: MutableLiveData<Resource<String>>? = MutableLiveData(null)
    val insertStationAlarm: LiveData<Resource<String>>? = _insertStationAlarm


    fun onBtnAddStationAlarmClicked(view:View) {
        btnAddStationAlarmClicked.postValue(true)
    }

    fun onbtnChooseStationClicked(view: View) {
        btnChooseStationClicked.postValue(true)
    }

    fun insertNewStationAlarmIntoDatabase(stationAlarmEntity: StationAlarmEntity) {
        viewModelScope.launch {
            _insertStationAlarm!!.value = Resource.Loading
            val child1 = launch(Dispatchers.IO) {
                insertNewStationAlarm(stationAlarmEntity) {
                    val child2 = launch(Dispatchers.Main) {
                        _insertStationAlarm!!.value = it
                    }
                }
            }
            child1.join()
        }
    }
}