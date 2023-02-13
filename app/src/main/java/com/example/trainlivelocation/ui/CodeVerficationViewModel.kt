package com.example.trainlivelocation.ui

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CodeVerficationViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
):ViewModel (){
    var codeVerfication:String?=null
}