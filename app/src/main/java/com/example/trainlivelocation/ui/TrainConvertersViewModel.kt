package com.example.trainlivelocation.ui

import Resource
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import com.example.trainlivelocation.BR
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.*
import com.example.domain.entity.Location_Response
import com.example.domain.entity.TrainConverterModel
import com.example.domain.entity.TrainResponseItem
import com.example.domain.usecase.GetAllTrains
import com.example.domain.usecase.GettingTrainlocationFromApi
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TrainConvertersViewModel @Inject constructor(
    private val getAllTrains: GetAllTrains,
    private val gettingTrainlocationFromApi: GettingTrainlocationFromApi
) :ViewModel() ,Observable{

    val takeoffStationTxtClicked= SingleLiveEvent<Boolean>()
    val arrivalStationTxtClicked= SingleLiveEvent<Boolean>()

    val userSelectedArrivalStation=MutableLiveData<String>()
    val userSelectedTakeOffStation=MutableLiveData<String>()
    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()
     var _FromTime: String = ""
     var _toTime: String = ""

    private val _trains: MutableLiveData<Resource<ArrayList<TrainResponseItem>>?> =
        MutableLiveData(null)
    val trains: LiveData<Resource<ArrayList<TrainResponseItem>>?> =
        _trains

    private val _trainLocation: MutableLiveData<Resource<Location_Response>?> =
        MutableLiveData(null)
    val trainLocation: LiveData<Resource<Location_Response>?> =
        _trainLocation




    @get:Bindable
    var fromTime: String
        get() = _FromTime
        set(value) {
            if (_FromTime != value) {
                _FromTime = value
                notifyPropertyChanged(BR.fromTime)
            }
        }

    @get:Bindable
    var toTime: String
        get() = _toTime
        set(value) {
            if (_toTime != value) {
                _toTime = value
                notifyPropertyChanged(BR.toTime)
            }
        }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        callbacks.remove(callback)
    }

    private fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }



    fun onTakeoffStationTxtClicked(view: View){
        takeoffStationTxtClicked.postValue(true)
    }

    fun onArrivalStationTxtClicked(view: View){
        arrivalStationTxtClicked.postValue(true)
    }

    fun getTrains(){
        viewModelScope.launch {
            _trains.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                getAllTrains(){
                    val chaild2=launch {
                        _trains.value=it
                    }
                }
            }
            child1.join()
        }
    }

    fun getTrainLocation(trainId:Int?){
        viewModelScope.launch {
            _trainLocation.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                gettingTrainlocationFromApi(trainId!!){
                    val chaild2=launch {
                        _trainLocation.value=it
                    }
                }
            }
            child1.join()
        }
    }





}