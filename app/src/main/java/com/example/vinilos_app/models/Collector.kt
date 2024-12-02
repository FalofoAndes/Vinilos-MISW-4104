package com.example.vinilos_app.models

data class Collector (
    val collectorId: Int,
    val name:String,
    val telephone:String,
    val email:String
)


data class CollectorDetail(
    val collectorId: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val comments: List<Comment>,
    val favoritePerformers: List<Performer>
)