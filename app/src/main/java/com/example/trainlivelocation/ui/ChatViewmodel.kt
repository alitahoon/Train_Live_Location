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
import com.example.domain.entity.NotificationTokenResponse
import com.example.domain.entity.PushPostNotification
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetChatFromFirebase
import com.example.domain.usecase.GetNotificationTokenByUserIDFromApi
import com.example.domain.usecase.PushAddPostNotification
import com.example.domain.usecase.SendMessageToFirebasechat
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewmodel @Inject constructor(
    private val sendMessageToFirebasechat: SendMessageToFirebasechat,
    private val context: Context,
    private val getChatFromFirebase: GetChatFromFirebase,
    private val getNotificationTokenByUserIDFromApi: GetNotificationTokenByUserIDFromApi,
    private val pushAddPostNotification: PushAddPostNotification
) : ViewModel() {
    var btnSendMessageClicked = SingleLiveEvent<Boolean>()
    var message:String?=" "



    private val _messageSend: MutableLiveData<Resource<String?>> = MutableLiveData(null)
    val messageSend: LiveData<Resource<String?>> = _messageSend

    private val _chatMessages: MutableLiveData<Resource<ArrayList<Message>?>> = MutableLiveData(null)
    val chatMessages: LiveData<Resource<ArrayList<Message>?>> = _chatMessages



  private val _NotificationToken: MutableLiveData<Resource<NotificationTokenResponse>> = MutableLiveData(null)
    val NotificationToken: LiveData<Resource<NotificationTokenResponse>> = _NotificationToken


    private val _Notification: MutableLiveData<Resource<String>> = MutableLiveData(null)
    val Notification: LiveData<Resource<String>> = _Notification




    fun sendMessage(sender:String?,reciever:String?,senderUsername:String?,recieverUsername:String?){
        _messageSend.value=Resource.Loading
        viewModelScope.launch {
            sendMessageToFirebasechat(message,sender,reciever,senderUsername,recieverUsername){
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


    fun getUserToken(userID:Int){
        viewModelScope.launch {
            _NotificationToken.value=Resource.Loading
            val child1=launch(Dispatchers.IO) {
                getNotificationTokenByUserIDFromApi(userID){
                    val child2=launch (Dispatchers.Main){
                        _NotificationToken.value=it
                    }
                }
            }
            child1.join()
        }
    }

    fun sendUserNotification(pushPostNotification: PushPostNotification){
        viewModelScope.launch {
            _Notification.value=Resource.Loading
            val child1=launch(Dispatchers.IO) {
                pushAddPostNotification(pushPostNotification){
                    val child2=launch (Dispatchers.Main){
                        _Notification.value=it
                    }
                }
            }
            child1.join()
        }
    }
}