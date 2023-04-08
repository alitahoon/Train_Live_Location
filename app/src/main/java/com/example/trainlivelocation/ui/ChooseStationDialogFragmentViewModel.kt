package com.example.trainlivelocation.ui

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.StationResponseItem
import com.example.domain.usecase.GetAllStations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChooseStationDialogFragmentViewModel @Inject constructor(
    private val getAllStations: GetAllStations
):ViewModel(){
    private var staionName: String? =null

    private val _stationsData:MutableLiveData<Resource<ArrayList<StationResponseItem>?>> = MutableLiveData(null)
     val stationsData:LiveData<Resource<ArrayList<StationResponseItem>?>> = _stationsData


    fun getAllStationFromApi(){
        _stationsData.value=Resource.Loading
        viewModelScope.launch{
            getAllStations{
                _stationsData.value=it
            }
        }
    }

}