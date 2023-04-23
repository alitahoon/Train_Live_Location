package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Message
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetChatFromFirebase
import com.example.domain.usecase.SendMessageToFirebasechat
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewmodel @Inject constructor(
    private val sendMessageToFirebasechat: SendMessageToFirebasechat,
    private val context: Context,
    private val getChatFromFirebase: GetChatFromFirebase
) : ViewModel() {
    private val sharedPrefFile = "UserToken"
    var btnSendMessageClicked = SingleLiveEvent<Boolean>()
    var message:String?=" "


    private val _userData: MutableLiveData<UserResponseItem?> = MutableLiveData(null)
    val userData: LiveData<UserResponseItem?> = _userData

    private val _messageSend: MutableLiveData<Resource<String?>> = MutableLiveData(null)
    val messageSend: LiveData<Resource<String?>> = _messageSend

    private val _chatMessages: MutableLiveData<Resource<ArrayList<Message>?>> = MutableLiveData(null)
    val chatMessages: LiveData<Resource<ArrayList<Message>?>> = _chatMessages


    fun getUserDataFromsharedPreference() {
        val userSharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        _userData.postValue(
            UserResponseItem(
                userSharedPreferences.getString("userAddress","empty")!!,
                userSharedPreferences.getString("userBirthdate","empty")!!,
                userSharedPreferences.getString("userEmail","empty")!!,
                userSharedPreferences.getString("userGender","empty")!!,
                userSharedPreferences.getInt("userId",0),
                userSharedPreferences.getString("userJop","empty")!!,
                userSharedPreferences.getString("userName","empty")!!,
                userSharedPreferences.getString("userPassword","empty")!!,
                userSharedPreferences.getString("userPhone","empty")!!,
                userSharedPreferences.getString("userRole","empty")!!,
                userSharedPreferences.getInt("userStationId",0)
            )
        )
    }

    fun sendMessage(sender:String?,reciever:String?){
        _messageSend.value=Resource.Loading
        viewModelScope.launch {
            sendMessageToFirebasechat(message,sender,reciever){
                _messageSend.value=it
            }
        }
    }

    fun getChat(sender: String?,reciever: String?){
        _chatMessages.value=Resource.Loading
        viewModelScope.launch {
            getChatFromFirebase(sender,reciever){
                _chatMessages.value=it
            }
        }
    }

    fun onBtnSendMessageClicked(view: View){
        btnSendMessageClicked.postValue(true)
    }
}