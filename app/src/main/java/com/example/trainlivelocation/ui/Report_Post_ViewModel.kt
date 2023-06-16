package com.example.trainlivelocation.ui

import Resource
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.ReportPostResponse
import com.example.domain.usecase.GetAllReport
import com.example.domain.usecase.ReportPost
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Report_Post_ViewModel @Inject constructor(
    private val reportPost: ReportPost,
    private val getAllReport: GetAllReport
) :ViewModel() {

    val btnYesClicked= SingleLiveEvent<Boolean>()
    val btnCancelClicked= SingleLiveEvent<Boolean>()

    private val _report:MutableLiveData<Resource<String>> = MutableLiveData(null)
     val report:LiveData<Resource<String>> = _report


    private val _allReports:MutableLiveData<Resource<ArrayList<ReportPostResponse>>> = MutableLiveData(null)
     val allReports:LiveData<Resource<ArrayList<ReportPostResponse>>> = _allReports


    fun report(postID:Int,userID:Int,resoen:String){
        _report.value=Resource.Loading
        viewModelScope.launch {
            val child1= launch (Dispatchers.IO){
                val child2=launch (Dispatchers.Main){
                    reportPost(postID,userID,resoen){
                        _report.value=it
                    }
                }

            }
            child1.join()

        }
    }

    fun getAllReports(){
        _allReports.value=Resource.Loading
        viewModelScope.launch {
            val child1= launch (Dispatchers.IO){
                val child2=launch (Dispatchers.Main){
                    getAllReport(){
                        _allReports.value=it
                    }
                }

            }
            child1.join()

        }
    }


    fun onBtnYesclciked(view: View){
        btnYesClicked.postValue(true)
    }

    fun onBtnCancelClicked(view: View){
        btnCancelClicked.postValue(true)
    }
}