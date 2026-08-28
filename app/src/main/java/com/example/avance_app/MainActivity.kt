package com.example.avance_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
        VistaFavoritos()
        VistaGrupos()
    }

    //Configuraciones de Pantallas
    // Pestaña Todos
    private fun VistaTodos() {
        tvTodos = findViewById(R.id.tvTodos)
        tvFavoritos = findViewById(R.id.tvFavoritos)
        tvGrupos = findViewById(R.id.tvGrupos)
        btnAgregarContacto = findViewById<Button>(R.id.btnAgregarContacto)

        mostrarContactos(ContactManager.showContacts())

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
    private fun VistaGrupos() {
        tvTodos = findViewById(R.id.tvTodos)
        tvFavoritos = findViewById(R.id.tvFavoritos)
        tvGrupos = findViewById(R.id.tvGrupos)

        // Por ahora, solo contiene la navegacion entre pestañas

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

            actualizarEstrella(estrella, contact.favorito)

            // Al hacer click en el icono, el objeto enntra en la lista de favoritos
            estrella.setOnClickListener {
                ContactManager.marcarFav(contact.id)
                val newState = ContactManager.showContacts().find { it.id == contact.id }?.favorito ?: false
                actualizarEstrella(estrella, newState)
            }

            estrella.setOnHoverListener { _, event ->
                when (event.action) {
                    android.view.MotionEvent.ACTION_HOVER_ENTER -> {
                        estrella.setColorFilter(android.graphics.Color.parseColor("#FFC107"))
                    }
                    android.view.MotionEvent.ACTION_HOVER_EXIT -> {
                        val actualState = ContactManager.showContacts().find { it.id == contact.id }?.favorito ?: false
                        actualizarEstrella(estrella, actualState)
                    }
                }
                false
            }

            fila.addView(avatar)
            fila.addView(columna)
            fila.addView(estrella)

            box.addView(fila) // Por cada objeto, se añade el recuadro de diseño correspondiente
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

    // Funcion para mostrar la lista de favoritos con el diseño correspondiente a la interfaz
    private fun mostrarFavoritos(lista: List<Contact>) {
        val boxF = findViewById<LinearLayout>(R.id.boxFavoritos)

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

            val estrella = android.widget.ImageView(this)
            estrella.setImageResource(android.R.drawable.btn_star_big_on)
            estrella.setColorFilter(android.graphics.Color.parseColor("#FFC107"))
            val tamanoEstrella = (22 * resources.displayMetrics.density).toInt()
            estrella.layoutParams = LinearLayout.LayoutParams(tamanoEstrella, tamanoEstrella)

            fila.addView(avatar)
            fila.addView(columna)

            boxF.addView(fila) // Por cada objeto, se añade el recuadro de diseño correspondiente
        }
    }
}