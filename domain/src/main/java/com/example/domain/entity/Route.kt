package com.example.domain.entity

data class Route(
    val summary: Summary,
    val segments: List<Segment>,
    val bbox: List<Double>,
    val geometry: String,
    val way_points: List<Int>
)