package com.example.trainlivelocation.ui

import Resource
import androidx.lifecycle.*
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
    val  staionName= MutableLiveData<String>()

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




    inline fun <T> LiveData<T>.filter(crossinline filter: (T?) -> Boolean): LiveData<T> {
        return MediatorLiveData<T>().apply {
            addSource(this@filter) {
                if (filter(it)) {
                    this.value = it
                }
            }
        }
    }

}