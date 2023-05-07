package com.example.domain.entity

import com.example.domain.usecase.SendOtpToPhone

class NotificatonToken(val userPhone:String?,val username:String?,val token:String?) {
    constructor():this("","","")
}