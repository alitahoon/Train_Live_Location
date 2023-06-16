package com.example.domain.entity

data class DirectionResponse(val routes: List<Route>,
                             val bbox: List<Double>,
                             val metadata: Metadata)
