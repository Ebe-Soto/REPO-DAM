package com.example.avance_app.data

import android.R
import android.widget.Filterable

interface Filtrable {
    fun obtenerFavs(): List<Contact>
}

// Se incluyen todas las funciones de manejo/control a partir del data de Contacto
object ContactManager: Filtrable{
    private val contacts = mutableListOf<Contact>()
    private var nextId = 1

    // Funcion para agregar contactos
    fun addContact(name: String, number: String, email: String): Contact {
        val newContact = Contact(
            id = nextId,
            name = name,
            number = number,
            email = email
        )
        contacts.add(newContact)
        nextId++
        return newContact
    }

    // Funcion para mostrar contactos
    fun showContacts() : List<Contact> = contacts

    fun marcarFav(id: Int) {
        val contacto = contacts.find { it.id == id }
        if (contacto != null) {
            contacto.favorito = true
        }
    }

    // Funcion de order superior con Lambda para obtener los elementos favoritos
    override fun obtenerFavs(): List<Contact> {
        return contacts.filter { it.favorito }
    }

}