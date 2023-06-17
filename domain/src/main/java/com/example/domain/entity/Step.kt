package com.example.domain.entity

data class Step(
    val distance: Double,
    val duration: Double,
    val type: Int,
    val instruction: String,
    val name: String,
    val way_points: List<Int>
)