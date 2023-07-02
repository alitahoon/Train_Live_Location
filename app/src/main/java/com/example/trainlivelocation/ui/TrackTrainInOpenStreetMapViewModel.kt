package com.example.trainlivelocation.ui

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.StationItemEntity
import com.example.domain.entity.StationResponseItem
import com.example.domain.usecase.GetAllStations
import com.example.domain.usecase.GetAllStationsFromDatabase
import com.example.domain.usecase.InsertNewStationToDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackTrainInOpenStreetMapViewModel @Inject constructor(
    private val getAllStationsFromDatabase: GetAllStationsFromDatabase,
    private val getAllStations: GetAllStations,
    private val insertNewStationToDatabase: InsertNewStationToDatabase,

    ) : ViewModel() {


    private val _stations: MutableLiveData<Resource<ArrayList<StationResponseItem>>?> =
        MutableLiveData(null)
    val stations: LiveData<Resource<ArrayList<StationResponseItem>>?> = _stations

    private val _getStationsFromDatabase: MutableLiveData<Resource<ArrayList<StationItemEntity>>?> =
        MutableLiveData(null)
    val getStationsFromDatabase: LiveData<Resource<ArrayList<StationItemEntity>>?> = _getStationsFromDatabase


    private val _insertingStationsToDatabase: MutableLiveData<Resource<String>?> =
        MutableLiveData(null)
    val insertingStationsToDatabase: LiveData<Resource<String>?> = _insertingStationsToDatabase

    fun getAllStation() {
        viewModelScope.launch {
            _stations.value = Resource.Loading
            val child1 = launch(Dispatchers.IO) {
                getAllStations {
                    val child2 = launch(Dispatchers.Main) {
                        _stations.value = it
                    }
                }
            }
            child1.join()
        }
    }

    fun gettingStationsFromDatabase(){
        viewModelScope.launch {
            _getStationsFromDatabase.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                getAllStationsFromDatabase(){
                    val child2=launch(Dispatchers.Main) {
                        _getStationsFromDatabase.value=it
                    }
                }
            }
            child1.join()
        }
    }


    fun insertingNewStationsToDatabase(stationItemEntity: StationItemEntity){
        viewModelScope.launch {
            _insertingStationsToDatabase.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                insertNewStationToDatabase(stationItemEntity){
                    val child2=launch(Dispatchers.Main) {
                        _insertingStationsToDatabase.value=it
                    }
                }
            }
            child1.join()
        }
    }
}