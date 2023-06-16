package com.example.domain.entity

data class Metadata(
    val attribution: String,
    val service: String,
    val timestamp: Long,
    val query: Query,
    val engine: Engine
)