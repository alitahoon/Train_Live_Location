package com.example.trainlivelocation.ui

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.DoctorResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PassengersViewModel @Inject constructor(

):ViewModel(){
    private val _passengers: MutableLiveData<Resource<ArrayList<DoctorResponseItem>>?> = MutableLiveData(null)
    val passengers: LiveData<Resource<ArrayList<DoctorResponseItem>>?> = _passengers
}