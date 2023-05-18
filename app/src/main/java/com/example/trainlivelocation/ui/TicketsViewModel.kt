package com.example.trainlivelocation.ui

import Resource
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.TicketRequestItem
import com.example.domain.entity.TicketResponseItem
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.CreateTicket
import com.example.domain.usecase.GetDataFromSharedPrefrences
import com.example.domain.usecase.SubscribeToNewTopic
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val createTicket: CreateTicket,
    private val getDataFromSharedPrefrences: GetDataFromSharedPrefrences,
    private val subscribeToNewTopic: SubscribeToNewTopic
):ViewModel() {
    var takroffStationTxtClicked = SingleLiveEvent<Boolean>()
    var arrivalStationTxtClicked = SingleLiveEvent<Boolean>()
    var trainIdTxtClicked = SingleLiveEvent<Boolean>()
    var btnAddTicketClicked = SingleLiveEvent<Boolean>()
    var userStation:String?=null


    private val _ticket:MutableLiveData<Resource<TicketResponseItem>> = MutableLiveData(null)
        val ticket:LiveData<Resource<TicketResponseItem>> = _ticket

    private val _subscribeTrain:MutableLiveData<Resource<String>> = MutableLiveData(null)
    val subscribeTrain:LiveData<Resource<String>> = _subscribeTrain




    fun onTakroffStationTxtClicked(view: View){
        takroffStationTxtClicked.postValue(true)
    }

    fun onArrivalStationTxtClicked(view: View){
        arrivalStationTxtClicked.postValue(true)
    }

    fun onTrainIdTxtClicked(view: View){
        trainIdTxtClicked.postValue(true)
    }

    fun onBtnAddTicketClicked(view: View){
        btnAddTicketClicked.postValue(true)
    }
    fun addTicketToApi(ticket:TicketRequestItem){
        _ticket.value=Resource.Loading
        viewModelScope.launch (){
            createTicket(ticket){
                _ticket.value=it
            }
        }
    }


    fun subscribeTrain(trainId:Int){
        _subscribeTrain.value=Resource.Loading
        viewModelScope.launch {
            subscribeToNewTopic("postAdded${trainId}"){
                _subscribeTrain.value=it
            }
            subscribeToNewTopic("doctors${trainId}"){
                _subscribeTrain.value=it
            }
        }
    }

}