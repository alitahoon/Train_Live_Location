package com.example.domain.entity

data class Station(
    val name: String,
    val nextStation: String,
    val description: String,
    val trainId: Int
)
