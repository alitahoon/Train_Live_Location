package com.example.domain.entity

import java.util.*

data class Payment(

    val id:Int,
    val bankName:String,
    val cardNumber:String,
    val cost:Int,
    val date: Date,
    val userId:Int

    )
