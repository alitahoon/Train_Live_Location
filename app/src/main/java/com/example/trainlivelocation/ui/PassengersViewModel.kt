package com.example.trainlivelocation.ui

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.DoctorResponseItem
import com.example.domain.entity.UserInTrainResponseItem
import com.example.domain.usecase.GetUserInTrain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PassengersViewModel @Inject constructor(
    private val getUserInTrain: GetUserInTrain
):ViewModel(){
    private val _passengers: MutableLiveData<Resource<ArrayList<UserInTrainResponseItem>>?> = MutableLiveData(null)
    val passengers: LiveData<Resource<ArrayList<UserInTrainResponseItem>>?> = _passengers

    fun getPassengers(trainId:Int?){
        _passengers.value = Resource.Loading
        viewModelScope.launch {
            getUserInTrain(trainId!!){
                _passengers.value = it
            }
        }
    }

}