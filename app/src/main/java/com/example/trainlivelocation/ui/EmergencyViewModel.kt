package com.example.trainlivelocation.ui

import Resource
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.DoctorResponseItem
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetDoctorInTrain
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class EmergencyViewModel @Inject constructor(
    private val getDoctorInTrain: GetDoctorInTrain
):ViewModel() {

    private val _doctors: MutableLiveData<Resource<ArrayList<DoctorResponseItem>>?> = MutableLiveData(null)
    val doctors: LiveData<Resource<ArrayList<DoctorResponseItem>>?> = _doctors

    var txtChooseTrainIdClicked = SingleLiveEvent<Boolean>()
    var trainid: String? = null

    fun ontxtChooseTrainIdClicked(view: View){
        txtChooseTrainIdClicked.postValue(true)
    }

    fun getDoctors(trainId:Int?){
        _doctors.value=Resource.Loading
        viewModelScope.launch {
            getDoctorInTrain(trainId!!){
                _doctors.value=it
            }
        }
    }
}