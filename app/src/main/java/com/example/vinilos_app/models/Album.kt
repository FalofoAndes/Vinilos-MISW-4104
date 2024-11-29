package com.example.vinilos_app.models

data class Album (
    val albumId:Int,
    val name:String,
    val cover:String,
    val releaseDate:String,
    val description:String,
    val genre:String,
    val recordLabel:String,
    val tracks: MutableList<Track>,
    val performers: List<Performer>,
    val comments: List<Comment>
)