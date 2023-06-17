package com.example.trainlivelocation.ui

import Resource
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.StationAlarmEntity
import com.example.domain.usecase.DeleteStationAlarmFromDatabase
import com.example.domain.usecase.GetStationAlarmsFromDatabase
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class Alarms_viewModel @Inject constructor(
    private val getStationAlarmsFromDatabase: GetStationAlarmsFromDatabase,
    private val deleteStationAlarmFromDatabase: DeleteStationAlarmFromDatabase
):ViewModel() {

    var btnAddAlarmClicked = SingleLiveEvent<Boolean>()

    private val _alarms: MutableLiveData<Resource<ArrayList<StationAlarmEntity>>>? = MutableLiveData(null)
    val alarms: LiveData<Resource<ArrayList<StationAlarmEntity>>>? = _alarms

    private val _deleteAlarm: MutableLiveData<Resource<String>>? = MutableLiveData(null)
    val deleteAlarm: LiveData<Resource<String>>? = _deleteAlarm

    fun onBtnAddAlarmClicked(view:View){
        btnAddAlarmClicked.postValue(true)
    }

    fun deleteAlarmFromDatabase(alarmId:Long){
        viewModelScope.launch {
            _deleteAlarm!!.value=Resource.Loading
            val child1= launch (Dispatchers.IO){
                deleteStationAlarmFromDatabase(alarmId){
                    val child2= launch (Dispatchers.Main){
                        _deleteAlarm.value=it
                    }
                }
            }
            child1.join()
        }    }

    fun getAlarmsFromDatabase(){
        viewModelScope.launch {
            _alarms!!.value=Resource.Loading
            val child1= launch (Dispatchers.IO){
                getStationAlarmsFromDatabase{
                    val child2= launch (Dispatchers.Main){
                        _alarms.value=it
                    }
                }
            }
            child1.join()
        }
    }
}