package com.example.trainlivelocation.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Add_post_fragment_ViewModel @Inject constructor(

) : ViewModel() {
    val postContent: String? = null
    val post_redio_checked = MutableLiveData<String>()

    fun setCritical(view : View){
        post_redio_checked.postValue("Critical")
    }
    fun setNONCritical(view : View){
        post_redio_checked.postValue("non_Critical")
    }
    fun onSubmitButtonClick(view : View){

    }
}