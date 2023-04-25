package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Message
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetDataFromSharedPrefrences
import com.example.domain.usecase.GetInboxRecieveChatFromFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxRecieveViewModel @Inject constructor(
    private val context: Context,
    private val getInboxRecieveChatFromFirebase: GetInboxRecieveChatFromFirebase,
    private val getDataFromSharedPrefrences: GetDataFromSharedPrefrences

) :ViewModel(){
    private val sharedPrefFile = "UserToken"
    private val TAG:String?="InboxRecieveViewModel"

    private val _inboxRecicve: MutableLiveData<Resource<ArrayList<Message>>>? = MutableLiveData(null)
    val inboxRecicve: LiveData<Resource<ArrayList<Message>>>? = _inboxRecicve

    private val _userData: MutableLiveData<Resource<UserResponseItem>?> = MutableLiveData(null)
    val userData: LiveData<Resource<UserResponseItem>?> = _userData


    fun getInboxRecieve(phone:String?){
        Log.i(TAG,"getInbox")
        _inboxRecicve!!.value=Resource.Loading
        viewModelScope.launch {
            getInboxRecieveChatFromFirebase(phone){
                _inboxRecicve.value=it
            }
        }
    }

    fun getUserDataFromsharedPreference() {
        _userData.value=Resource.Loading
        viewModelScope.launch {
            getDataFromSharedPrefrences(sharedPrefFile){
                _userData.value=it
            }

        }
    }

}