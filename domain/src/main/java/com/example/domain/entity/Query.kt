package com.example.domain.entity

data class Query(
    val coordinates: List<List<Double>>,
    val profile: String,
    val format: String
)