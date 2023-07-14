package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.*
import com.example.domain.usecase.*
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
    private val pushSendMessageNotification: PushSendMessageNotification,
    private val getUserTokenByPhoneNumber: GetUserTokenByPhoneNumber
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


    private val _userTokenByPhoneNumber: MutableLiveData<Resource<String>> = MutableLiveData(null)
    val userTokenByPhoneNumber: LiveData<Resource<String>> = _userTokenByPhoneNumber





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

    fun getToken(phoneNumber:String?){
        viewModelScope.launch {
            _userTokenByPhoneNumber.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                getUserTokenByPhoneNumber(phoneNumber!!){
                    val child2=launch (Dispatchers.Main){
                        _userTokenByPhoneNumber.value=it
                    }
                }
            }
            child1.join()
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

    fun sendUserNotification(pushMessageNotification: PushMessageNotification){
        viewModelScope.launch {
            _Notification.value=Resource.Loading
            val child1=launch(Dispatchers.IO) {
                pushSendMessageNotification(pushMessageNotification){
                    val child2=launch (Dispatchers.Main){
                        _Notification.value=it
                    }
                }
            }
            child1.join()
        }
    }
}