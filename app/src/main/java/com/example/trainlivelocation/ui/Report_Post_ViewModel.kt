package com.example.trainlivelocation.ui

import Resource
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.ReportPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Report_Post_ViewModel @Inject constructor(
    private val reportPost: ReportPost
) :ViewModel() {

//    private val _report:MutableLiveData<Resource<>> = MutableLiveData(null)
//     val report:LiveData<Resource<>> = _report


    fun report(postID:Int,userID:Int,resoen:String){
//        _report.value=Resource.Loading
//        viewModelScope.launch {
//            reportPost(postID,userID,resoen){
//                _report.value=it
//            }
//        }
    }
}