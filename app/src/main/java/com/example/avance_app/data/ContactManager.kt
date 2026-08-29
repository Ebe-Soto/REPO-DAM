package com.example.avance_app.data

import android.R

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
            contacto.favorito = !contacto.favorito
        }
    }

    // Funcion de order superior con Lambda para obtener los elementos favoritos
    override fun obtenerFavs(): List<Contact> {
        return contacts.filter { it.favorito }
    }

    fun buscarContacto(query: String): List<Contact> {
        return contacts.filter { it.name.contains(query, ignoreCase = true) }
    }

    fun editarContacto(id: Int, name: String, number: String, email: String) {
        val contacto = contacts.find { it.id == id }
        if (contacto != null) {
            contacto.name = name
            contacto.number = number
            contacto.email = email
        }
    }
    fun delContacto(id: Int) {
        contacts.removeIf { it.id == id }
        // Lo quitamos también de cualquier grupo al que pertenecía
        groups.forEach { it.memberIds.remove(id) }
    }

    fun totalContactos(): Int = contacts.size

    // ---- GRUPOS ----
    private val groups = mutableListOf<Group>()

    fun crearGrupo(nombre: String, miembros: List<Int> = emptyList()): Group {
        val nuevoGrupo = Group(
            id = java.util.UUID.randomUUID().toString(),
            name = nombre,
            memberIds = miembros.toMutableList()
        )
        groups.add(nuevoGrupo)
        return nuevoGrupo
    }

    fun showGroups(): List<Group> = groups

    fun obtenerGrupo(id: String): Group? = groups.find { it.id == id }

    fun obtenerMiembrosDeGrupo(groupId: String): List<Contact> {
        val grupo = obtenerGrupo(groupId) ?: return emptyList()
        return contacts.filter { grupo.memberIds.contains(it.id) }
    }

    fun buscarGrupo(query: String): List<Group> =
        groups.filter { it.name.contains(query, ignoreCase = true) }
}