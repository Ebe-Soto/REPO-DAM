package com.example.avance_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.avance_app.data.Contact
import com.example.avance_app.data.ContactManager

class MainActivity : AppCompatActivity() {
    // Variables de las vistas de navegación, se reutilizan en cada pantalla
    private lateinit var tvTodos: TextView
    private lateinit var tvFavoritos: TextView
    private lateinit var tvGrupos: TextView
    private lateinit var btnAgregarContacto: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactos)

        VistaTodos()
    }

    //Configuraciones de Pantallas
    // Pestaña Todos
    private fun VistaTodos() {
        tvTodos = findViewById(R.id.tvTodos)
        tvFavoritos = findViewById(R.id.tvFavoritos)
        tvGrupos = findViewById(R.id.tvGrupos)
        btnAgregarContacto = findViewById<Button>(R.id.btnAgregarContacto)
        val etBuscarContacto = findViewById<EditText>(R.id.etBuscarContacto)
        val tvTotalContactos = findViewById<TextView>(R.id.tvTotalContactos)

        mostrarContactos(ContactManager.showContacts())
        actualizarContador(tvTotalContactos)

        // Búsqueda de Contacto utilizando una funcion de busqueda en tiempo real
        etBuscarContacto.doAfterTextChanged { texto ->
            val resultados = ContactManager.buscarContacto(texto.toString())
            mostrarContactos(resultados)
        }

        tvTodos.setOnClickListener {
            setContentView(R.layout.activity_contactos)
            VistaTodos()

        }
        tvFavoritos.setOnClickListener {
            setContentView(R.layout.activity_favoritos)
            VistaFavoritos()
        }
        tvGrupos.setOnClickListener {
            setContentView(R.layout.activity_grupos)
            VistaGrupos()
        }

        // Al presionar el boton de Guardar, volvemos a la pantalla principal y se crea el objeto
        btnAgregarContacto.setOnClickListener {
            setContentView(R.layout.dialog_agregar_contacto)
            CrearContacto()
        }

    }

    // Pestaña Favoritos
    private fun VistaFavoritos() {
        tvTodos = findViewById(R.id.tvTodos)
        tvFavoritos = findViewById(R.id.tvFavoritos)
        tvGrupos = findViewById(R.id.tvGrupos)

        // Función para mostrar favoritos filtrando desde el metodo obtenerFavs
        mostrarFavoritos(ContactManager.obtenerFavs())

        tvTodos.setOnClickListener {
            setContentView(R.layout.activity_contactos)
            VistaTodos()
        }
        tvFavoritos.setOnClickListener {
            setContentView(R.layout.activity_favoritos)
            VistaFavoritos()
        }
        tvGrupos.setOnClickListener {
            setContentView(R.layout.activity_grupos)
            VistaGrupos()
        }
    }

    // Pestaña Grupos
    // Pestaña Grupos
    private fun VistaGrupos() {
        tvTodos = findViewById(R.id.tvTodos)
        tvFavoritos = findViewById(R.id.tvFavoritos)
        tvGrupos = findViewById(R.id.tvGrupos)
        val fabCrearGrupo = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabCrearGrupo)
        val etBuscarGrupos = findViewById<EditText>(R.id.etBuscarGrupos)

        mostrarGrupos(ContactManager.showGroups())

        etBuscarGrupos.doAfterTextChanged { texto ->
            mostrarGrupos(ContactManager.buscarGrupo(texto.toString()))
        }

        fabCrearGrupo.setOnClickListener {
            setContentView(R.layout.dialog_crear_grupo)
            CrearGrupo()
        }

        tvTodos.setOnClickListener {
            setContentView(R.layout.activity_contactos)
            VistaTodos()
        }
        tvFavoritos.setOnClickListener {
            setContentView(R.layout.activity_favoritos)
            VistaFavoritos()
        }
        tvGrupos.setOnClickListener {
            setContentView(R.layout.activity_grupos)
            VistaGrupos()
        }
    }

    private fun actualizarContador(tv: TextView) {
        val total = ContactManager.totalContactos()
        tv.text = if (total == 1) "$total contacto" else "$total contactos"
    }

    private fun CrearContacto() {
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Lectura segura de campos de texto, uso de operador Elvis para evitar que la aplicacion truene
        btnGuardar.setOnClickListener {
            val name = etNombre.text.toString() ?: ""
            val email = etEmail.text.toString() ?: ""
            val number = etTelefono.text.toString()

            // Si el nombre está vacío, se lanza una excepcion activando una alerta
            try {
                if (name.isEmpty()){
                    throw IllegalArgumentException("El nombre no puede estar vacío")
                }

                ContactManager.addContact(name, number, email)

                Toast.makeText(this, "¡Contacto Guardado Exitosamente!", Toast.LENGTH_SHORT).show()

                etNombre.text.clear()
                etTelefono.text.clear()
                etEmail.text.clear()

                setContentView(R.layout.activity_contactos)
                VistaTodos()

            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para volver a la pantalla principal y cancelar la operacion
        btnCancelar.setOnClickListener {
            setContentView(R.layout.activity_contactos)
            VistaTodos()
        }


    }

    // Funcion para mostrar los contactos en filas con un diseño acorde a la interfaz
    private fun mostrarContactos(lista: List<Contact>) {
        val box = findViewById<LinearLayout>(R.id.boxContactos)
        box.removeAllViews()

        for (contact in lista) {
            val fila = LinearLayout(this)
            fila.orientation = LinearLayout.HORIZONTAL
            fila.gravity = android.view.Gravity.CENTER_VERTICAL
            fila.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val paddingVertical = (10 * resources.displayMetrics.density).toInt()
            val paddingHorizontal = (16 * resources.displayMetrics.density).toInt()
            fila.setPadding(paddingHorizontal, paddingVertical,paddingHorizontal, paddingVertical)

            val avatar = TextView(this)
            avatar.text = contact.name.take(2).uppercase()
            avatar.textSize = 13f
            avatar.setTypeface(null, android.graphics.Typeface.BOLD)
            avatar.gravity = android.view.Gravity.CENTER
            avatar.setBackgroundResource(R.drawable.bg_avatar_circle)
            avatar.backgroundTintList = resources.getColorStateList(R.color.avatar_purple_bg, theme)
            avatar.setTextColor(resources.getColor(R.color.avatar_purple_text, theme))
            val tamano = (40 * resources.displayMetrics.density).toInt()
            avatar.layoutParams = LinearLayout.LayoutParams(tamano, tamano)

            val columna = LinearLayout(this)
            columna.orientation = LinearLayout.VERTICAL
            val paramsColumna = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            paramsColumna.marginStart = (12 * resources.displayMetrics.density).toInt()
            columna.layoutParams = paramsColumna

            val nombre = TextView(this)
            nombre.text = contact.name
            nombre.textSize = 15f
            nombre.setTypeface(null, android.graphics.Typeface.BOLD)
            nombre.setTextColor(resources.getColor(R.color.text_primary, theme))

            val telefono = TextView(this)
            telefono.text = contact.number
            telefono.textSize = 13f
            telefono.setTextColor(resources.getColor(R.color.text_secondary, theme))

            columna.addView(nombre)
            columna.addView(telefono)

            // Icono de estrella para marcar favorito
            val estrella = android.widget.ImageView(this)
            val tamanoEstrella = (22 * resources.displayMetrics.density).toInt()
            estrella.layoutParams = LinearLayout.LayoutParams(tamanoEstrella, tamanoEstrella)

            // Estado inicial del ícono según el objeto (por si ya era favorito antes)
            actualizarEstrella(estrella, contact.favorito)

            // Al hacer click, el objeto entra/sale de favoritos (toggle)
            estrella.setOnClickListener {
                ContactManager.marcarFav(contact.id)
                val nuevoEstado = ContactManager.showContacts().find { it.id == contact.id }?.favorito ?: false
                actualizarEstrella(estrella, nuevoEstado)
            }

            fila.addView(avatar)
            fila.addView(columna)
            fila.addView(estrella)

            // Al hacer click en la fila del contacto, se revela el layout del contacto correspondiente
            fila.setOnClickListener {
                setContentView(R.layout.activity_detalle_contacto)
                VistaDC(contact)
            }

            box.addView(fila) // Por cada objeto, se añade el recuadro de diseño correspondiente
        }
    }

    // Funcion para mostrar la lista de favoritos con el diseño correspondiente a la interfaz
    private fun mostrarFavoritos(lista: List<Contact>) {
        val boxF = findViewById<LinearLayout>(R.id.boxFavoritos)
        boxF.removeAllViews()

        for (contact in lista) {
            val fila = LinearLayout(this)
            fila.orientation = LinearLayout.HORIZONTAL
            fila.gravity = android.view.Gravity.CENTER_VERTICAL
            fila.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val paddingVertical = (10 * resources.displayMetrics.density).toInt()
            val paddingHorizontal = (16 * resources.displayMetrics.density).toInt()
            fila.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

            val avatar = TextView(this)
            avatar.text = contact.name.take(2).uppercase()
            avatar.textSize = 13f
            avatar.setTypeface(null, android.graphics.Typeface.BOLD)
            avatar.gravity = android.view.Gravity.CENTER
            avatar.setBackgroundResource(R.drawable.bg_avatar_circle)
            avatar.backgroundTintList = resources.getColorStateList(R.color.avatar_purple_bg, theme)
            avatar.setTextColor(resources.getColor(R.color.avatar_purple_text, theme))
            val tamano = (40 * resources.displayMetrics.density).toInt()
            avatar.layoutParams = LinearLayout.LayoutParams(tamano, tamano)

            val columna = LinearLayout(this)
            columna.orientation = LinearLayout.VERTICAL
            val paramsColumna = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            paramsColumna.marginStart = (12 * resources.displayMetrics.density).toInt()
            columna.layoutParams = paramsColumna

            val nombre = TextView(this)
            nombre.text = contact.name
            nombre.textSize = 15f
            nombre.setTypeface(null, android.graphics.Typeface.BOLD)
            nombre.setTextColor(resources.getColor(R.color.text_primary, theme))

            val telefono = TextView(this)
            telefono.text = contact.number
            telefono.textSize = 13f
            telefono.setTextColor(resources.getColor(R.color.text_secondary, theme))

            columna.addView(nombre)
            columna.addView(telefono)
            fila.addView(avatar)
            fila.addView(columna)

            // Al hacer click en la fila del contacto, se revela el layout del contacto correspondiente
            fila.setOnClickListener {
                setContentView(R.layout.activity_detalle_contacto)
                VistaDC(contact)
            }

            boxF.addView(fila) // Por cada objeto, se añade el recuadro de diseño correspondiente
        }
    }

    private fun actualizarEstrella(estrella: android.widget.ImageView, esFavorito: Boolean) {
        if (esFavorito) {
            estrella.setImageResource(android.R.drawable.btn_star_big_on)
            estrella.setColorFilter(android.graphics.Color.parseColor("#FFC107"))
        } else {
            estrella.setImageResource(android.R.drawable.btn_star_big_off)
            estrella.clearColorFilter()
        }
    }

    // Declaración de layout individual de cada contacto
    private fun VistaDC(contact: Contact) {
        val tvNombreDC = findViewById<TextView>(R.id.tvNombreDC)
        val tvTelefonoDC = findViewById<TextView>(R.id.tvTelefonoDC)
        val tvEmailDC = findViewById<TextView>(R.id.tvEmailDC)
        val tvCDC = findViewById<TextView>(R.id.tvCuadroContacto)
        val btnVolverAC = findViewById<android.widget.ImageView>(R.id.btnVolverAContacto)
        val filaFavoritoDC = findViewById<LinearLayout>(R.id.filaFavoritoDC)
        val iconoFavoritoDC = findViewById<android.widget.ImageView>(R.id.iconoFavoritoDC)
        val tvFavoritoDC = findViewById<TextView>(R.id.tvFavoritoDC)
        val filaEliminar = findViewById<LinearLayout>(R.id.tvFilaDEL)
        val btnEditarContacto = findViewById<ImageView>(R.id.btnEditarContacto)

        // Llenar datos del contacto según el seleccionado
        tvNombreDC.text = contact.name
        tvTelefonoDC.text = contact.number
        tvEmailDC.text = contact.email
        tvCDC.text = contact.name.take(2).uppercase()

        // Estado inicial de favorito
        actualizarFavDC(iconoFavoritoDC, tvFavoritoDC, contact.favorito)

        // Toggle de favorito
        filaFavoritoDC.setOnClickListener {
            ContactManager.marcarFav(contact.id)
            val nuevoEstado = ContactManager.showContacts().find { it.id == contact.id }?.favorito ?: false
            actualizarFavDC(iconoFavoritoDC, tvFavoritoDC, nuevoEstado)
        }
        // Eliminar Contacto individualmente
        filaEliminar.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar contacto")
                .setMessage("¿Seguro que quieres eliminar a ${contact.name}? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    ContactManager.delContacto(contact.id)
                    Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show()
                    setContentView(R.layout.activity_contactos)
                    VistaTodos()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Regresar a la lista
        btnVolverAC.setOnClickListener {
            setContentView(R.layout.activity_contactos)
            VistaTodos()
        }

        btnEditarContacto.setOnClickListener {
            setContentView(R.layout.dialog_editar_contacto)
            EditarContacto(contact)
        }
    }

    private fun EditarContacto(contact: Contact) {
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Precargar datos actuales
        etNombre.setText(contact.name)
        etTelefono.setText(contact.number)
        etEmail.setText(contact.email)

        btnGuardar.setOnClickListener {
            val name = etNombre.text.toString()
            val email = etEmail.text.toString()
            val number = etTelefono.text.toString()

            try {
                if (name.isEmpty()) {
                    throw IllegalArgumentException("El nombre no puede estar vacío")
                }

                ContactManager.editarContacto(contact.id, name, number, email)

                Toast.makeText(this, "¡Contacto actualizado!", Toast.LENGTH_SHORT).show()

                setContentView(R.layout.activity_detalle_contacto)
                VistaDC(ContactManager.showContacts().find { it.id == contact.id }!!)

            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener {
            setContentView(R.layout.activity_detalle_contacto)
            VistaDC(contact)
        }
    }

    // Texto que cambia en el layout según corresponda
    private fun actualizarFavDC(icono: android.widget.ImageView, texto: TextView, esFavorito: Boolean) {
        if (esFavorito) {
            icono.setImageResource(android.R.drawable.btn_star_big_on)
            texto.text = "Quitar de favoritos"
        } else {
            icono.setImageResource(android.R.drawable.btn_star_big_off)
            texto.text = "Agregar a favoritos"
        }
    }

    // Dibuja las tarjetas de grupo dinámicamente (reemplaza la tarjeta de ejemplo del XML)
    private fun mostrarGrupos(lista: List<com.example.avance_app.data.Group>) {
        val container = findViewById<LinearLayout>(R.id.containerGrupos)
        container.removeAllViews()

        for (grupo in lista) {
            val card = com.google.android.material.card.MaterialCardView(this)
            val paramsCard = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            paramsCard.topMargin = (16 * resources.displayMetrics.density).toInt()
            card.layoutParams = paramsCard
            card.radius = 14 * resources.displayMetrics.density
            card.cardElevation = 2 * resources.displayMetrics.density
            card.isClickable = true
            card.isFocusable = true

            val fila = LinearLayout(this)
            fila.orientation = LinearLayout.HORIZONTAL
            fila.gravity = android.view.Gravity.CENTER_VERTICAL
            val padding = (14 * resources.displayMetrics.density).toInt()
            fila.setPadding(padding, padding, padding, padding)

            val icono = ImageView(this)
            val tamanoIcono = (40 * resources.displayMetrics.density).toInt()
            icono.layoutParams = LinearLayout.LayoutParams(tamanoIcono, tamanoIcono)
            icono.setBackgroundResource(R.drawable.bg_group_icon)
            icono.setImageResource(R.drawable.ic_group)
            icono.setPadding(padding / 2, padding / 2, padding / 2, padding / 2)
            icono.setColorFilter(resources.getColor(R.color.purple_primary, theme))

            val columna = LinearLayout(this)
            columna.orientation = LinearLayout.VERTICAL
            val paramsColumna = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            paramsColumna.marginStart = (12 * resources.displayMetrics.density).toInt()
            columna.layoutParams = paramsColumna

            val nombre = TextView(this)
            nombre.text = grupo.name
            nombre.textSize = 15f
            nombre.setTypeface(null, android.graphics.Typeface.BOLD)
            nombre.setTextColor(resources.getColor(R.color.text_primary, theme))

            val miembrosTv = TextView(this)
            val total = grupo.memberIds.size
            miembrosTv.text = if (total == 1) "1 miembro" else "$total miembros"
            miembrosTv.textSize = 13f
            miembrosTv.setTextColor(resources.getColor(R.color.text_secondary, theme))

            columna.addView(nombre)
            columna.addView(miembrosTv)
            fila.addView(icono)
            fila.addView(columna)
            card.addView(fila)

            // Acceder al grupo (punto 2 del pendiente)
            card.setOnClickListener {
                setContentView(R.layout.activity_detalle_grupo)
                VistaDetalleGrupo(grupo)
            }

            container.addView(card)
        }
    }

    // Pantalla de crear grupo: nombre + checklist de contactos (punto 1 y 3 del pendiente)
    private fun CrearGrupo() {
        val etNombreGrupo = findViewById<EditText>(R.id.etNombreGrupo)
        val containerCheck = findViewById<LinearLayout>(R.id.containerContactosCheck)
        val btnGuardarGrupo = findViewById<Button>(R.id.btnGuardarGrupo)
        val btnCancelarGrupo = findViewById<Button>(R.id.btnCancelarGrupo)

        containerCheck.removeAllViews()
        val checks = mutableMapOf<Int, android.widget.CheckBox>()

        for (contact in ContactManager.showContacts()) {
            val check = android.widget.CheckBox(this)
            check.text = "${contact.name} · ${contact.number}"
            check.textSize = 14f
            check.setTextColor(resources.getColor(R.color.text_primary, theme))
            checks[contact.id] = check
            containerCheck.addView(check)
        }

        btnGuardarGrupo.setOnClickListener {
            val nombre = etNombreGrupo.text.toString()
            try {
                if (nombre.isEmpty()) {
                    throw IllegalArgumentException("El nombre del grupo no puede estar vacío")
                }
                val seleccionados = checks.filter { it.value.isChecked }.keys.toList()
                ContactManager.crearGrupo(nombre, seleccionados)

                Toast.makeText(this, "¡Grupo creado exitosamente!", Toast.LENGTH_SHORT).show()
                setContentView(R.layout.activity_grupos)
                VistaGrupos()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelarGrupo.setOnClickListener {
            setContentView(R.layout.activity_grupos)
            VistaGrupos()
        }
    }

    // Detalle del grupo con miembros reales (punto 2 y 3 del pendiente)
    private fun VistaDetalleGrupo(grupo: com.example.avance_app.data.Group) {
        val btnAtras = findViewById<android.widget.ImageButton>(R.id.btnAtras)
        val tvNombreGrupo = findViewById<TextView>(R.id.tvNombreGrupo)
        val tvMiembrosActivos = findViewById<TextView>(R.id.tvMiembrosActivos)
        val btnLlamadaGrupal = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLlamadaGrupal)
        val btnMensajeGrupal = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnMensajeGrupal)
        val containerMiembros = findViewById<LinearLayout>(R.id.containerMiembros)

        val miembros = ContactManager.obtenerMiembrosDeGrupo(grupo.id)
        tvNombreGrupo.text = grupo.name
        tvMiembrosActivos.text = if (miembros.size == 1) "1 miembro" else "${miembros.size} miembros"

        containerMiembros.removeAllViews()
        for (contact in miembros) {
            val fila = LinearLayout(this)
            fila.orientation = LinearLayout.HORIZONTAL
            fila.gravity = android.view.Gravity.CENTER_VERTICAL
            val paddingV = (10 * resources.displayMetrics.density).toInt()
            fila.setPadding(0, paddingV, 0, paddingV)

            val avatar = TextView(this)
            avatar.text = contact.name.take(2).uppercase()
            avatar.textSize = 13f
            avatar.setTypeface(null, android.graphics.Typeface.BOLD)
            avatar.gravity = android.view.Gravity.CENTER
            avatar.setBackgroundResource(R.drawable.bg_avatar_circle)
            avatar.backgroundTintList = resources.getColorStateList(R.color.avatar_peach_bg, theme)
            avatar.setTextColor(resources.getColor(R.color.avatar_peach_text, theme))
            val tamano = (40 * resources.displayMetrics.density).toInt()
            avatar.layoutParams = LinearLayout.LayoutParams(tamano, tamano)

            val columna = LinearLayout(this)
            columna.orientation = LinearLayout.VERTICAL
            val paramsColumna = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            paramsColumna.marginStart = (12 * resources.displayMetrics.density).toInt()
            columna.layoutParams = paramsColumna

            val nombre = TextView(this)
            nombre.text = contact.name
            nombre.textSize = 14f
            nombre.setTypeface(null, android.graphics.Typeface.BOLD)
            nombre.setTextColor(resources.getColor(R.color.text_primary, theme))

            val telefono = TextView(this)
            telefono.text = contact.number
            telefono.textSize = 12f
            telefono.setTextColor(resources.getColor(R.color.text_secondary, theme))

            columna.addView(nombre)
            columna.addView(telefono)

            val btnLlamar = android.widget.ImageButton(this)
            val tamanoBtn = (34 * resources.displayMetrics.density).toInt()
            btnLlamar.layoutParams = LinearLayout.LayoutParams(tamanoBtn, tamanoBtn)
            btnLlamar.setImageResource(android.R.drawable.ic_menu_call)
            btnLlamar.setBackgroundResource(android.R.color.transparent)
            btnLlamar.setOnClickListener {
                Toast.makeText(this, "Llamando a ${contact.name}...", Toast.LENGTH_SHORT).show()
            }

            fila.addView(avatar)
            fila.addView(columna)
            fila.addView(btnLlamar)
            containerMiembros.addView(fila)
        }

        // Abre la llamada grupal (punto 4 del pendiente)
        btnLlamadaGrupal.setOnClickListener {
            setContentView(R.layout.activity_llamada_grupal)
            VistaLlamadaGrupal(grupo)
        }

        // La dejamos pendiente para el proyecto final, como comentó tu compañera
        btnMensajeGrupal.setOnClickListener {
            Toast.makeText(this, "Mensaje grupal: próximamente", Toast.LENGTH_SHORT).show()
        }

        btnAtras.setOnClickListener {
            setContentView(R.layout.activity_grupos)
            VistaGrupos()
        }
    }

    // Pantalla de llamada grupal con los miembros reales (punto 4 del pendiente)
    private fun VistaLlamadaGrupal(grupo: com.example.avance_app.data.Group) {
        val btnAtras = findViewById<android.widget.ImageButton>(R.id.btnAtras)
        val btnCerrar = findViewById<android.widget.ImageButton>(R.id.btnCerrar)
        val tvDestinatario = findViewById<TextView>(R.id.tvDestinatario)
        val containerParticipantes = findViewById<LinearLayout>(R.id.containerParticipantes)
        val btnCancelarLlamada = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCancelarLlamada)
        val btnIniciarLlamada = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnIniciarLlamada)

        tvDestinatario.text = grupo.name

        val miembros = ContactManager.obtenerMiembrosDeGrupo(grupo.id)
        containerParticipantes.removeAllViews()

        for (contact in miembros) {
            val columna = LinearLayout(this)
            columna.orientation = LinearLayout.VERTICAL
            columna.gravity = android.view.Gravity.CENTER
            val paramsColumna = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            paramsColumna.marginEnd = (16 * resources.displayMetrics.density).toInt()
            columna.layoutParams = paramsColumna

            val avatar = TextView(this)
            avatar.text = contact.name.take(2).uppercase()
            avatar.gravity = android.view.Gravity.CENTER
            avatar.setTypeface(null, android.graphics.Typeface.BOLD)
            avatar.setBackgroundResource(R.drawable.bg_avatar_circle)
            avatar.backgroundTintList = resources.getColorStateList(R.color.avatar_peach_bg, theme)
            avatar.setTextColor(resources.getColor(R.color.avatar_peach_text, theme))
            val tamano = (56 * resources.displayMetrics.density).toInt()
            avatar.layoutParams = LinearLayout.LayoutParams(tamano, tamano)

            val nombre = TextView(this)
            nombre.text = contact.name.split(" ").first()
            nombre.textSize = 11f
            nombre.gravity = android.view.Gravity.CENTER
            nombre.setTextColor(resources.getColor(R.color.text_primary, theme))

            columna.addView(avatar)
            columna.addView(nombre)
            containerParticipantes.addView(columna)
        }

        btnIniciarLlamada.setOnClickListener {
            Toast.makeText(this, "Iniciando llamada grupal con ${grupo.name}...", Toast.LENGTH_SHORT).show()
        }
        btnCancelarLlamada.setOnClickListener {
            setContentView(R.layout.activity_detalle_grupo)
            VistaDetalleGrupo(grupo)
        }
        btnAtras.setOnClickListener {
            setContentView(R.layout.activity_detalle_grupo)
            VistaDetalleGrupo(grupo)
        }
        btnCerrar.setOnClickListener {
            setContentView(R.layout.activity_contactos)
            VistaTodos()
        }
    }
}
