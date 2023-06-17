package com.example.domain.entity

data class OpenRouteDirectionResult(
    val polyline: String,
    val distance: Double,
    val duration: Double
)