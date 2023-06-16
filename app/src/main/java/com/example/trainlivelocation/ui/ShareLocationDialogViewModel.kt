package com.example.trainlivelocation.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainlivelocation.utli.SingleLiveEvent
import javax.inject.Inject

class ShareLocationDialogViewModel @Inject constructor(

) :ViewModel() {

    var btnYes = SingleLiveEvent<Boolean>()
    var btnNo = SingleLiveEvent<Boolean>()
    var txtTrainIdClicked = SingleLiveEvent<Boolean>()
    var trainId=MutableLiveData<String>()


    fun OnBtnYesClciked(view: View){
        btnYes.postValue(true)
    }
    fun OnBtnNoClciked(view: View){
        btnNo.postValue(true)
    }

    fun onTxtTrainIdClicked(view: View){
        txtTrainIdClicked.postValue(true)
    }

}