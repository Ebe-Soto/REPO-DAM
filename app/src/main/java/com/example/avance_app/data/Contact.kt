package com.example.avance_app.data

// Formato de atributos y tipos de dato
data class Contact(
    val id: Int,
    val name: String,
    val number: String,
    val email: String,
    var favorito: Boolean = false
)