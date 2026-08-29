package com.example.avance_app.data

data class Group(
    val id: String,
    var name: String,
    val memberIds: MutableList<Int> = mutableListOf()
)