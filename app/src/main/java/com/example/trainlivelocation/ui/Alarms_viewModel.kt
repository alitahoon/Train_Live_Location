package com.example.trainlivelocation.ui

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.StationAlarmEntity
import com.example.domain.usecase.GetStationAlarmsFromDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class Alarms_viewModel @Inject constructor(
    private val getStationAlarmsFromDatabase: GetStationAlarmsFromDatabase
):ViewModel() {
    private val _alarms: MutableLiveData<Resource<ArrayList<StationAlarmEntity>>>? = MutableLiveData(null)
    val alarms: LiveData<Resource<ArrayList<StationAlarmEntity>>>? = _alarms



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