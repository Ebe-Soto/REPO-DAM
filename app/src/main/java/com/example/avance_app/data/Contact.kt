package com.example.avance_app.data

// Formato de atributos y tipos de dato
data class Contact(
    val id: Int,
    var name: String,
    var number: String,
    var email: String,
    var favorito: Boolean = false
)