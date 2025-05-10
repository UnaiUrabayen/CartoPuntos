package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Activity_usuario : AppCompatActivity() {

    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvEmailUsuario: TextView
    private lateinit var tvTotalPlantillas: TextView
    private lateinit var btnPlantillas: Button
    private lateinit var btnLogout: ImageView
    private lateinit var btnBack: ImageView

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_usuario)

        // Enlazar vistas
        tvNombreUsuario = findViewById(R.id.tv_nombreUsuario)
        tvEmailUsuario = findViewById(R.id.tv_emailUsuario)
        tvTotalPlantillas = findViewById(R.id.tv_totalPartidas)
        btnPlantillas = findViewById(R.id.btn_plantillas)
        btnLogout = findViewById(R.id.imb_logout)
        btnBack = findViewById(R.id.imb_atras)

        // Verificar si el usuario está autenticado
        val user = auth.currentUser
        if (user != null) {
            // Si el usuario está autenticado, obtenemos su información
            val emailUsuario = user.email ?: "user@thx.com"

            // Obtener los detalles del usuario desde Firestore
            db.collection("usuarios")
                .document(emailUsuario)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nombreUsuario = document.getString("nombreUsuario") ?: "Usuario"
                        // Mostrar nombre, email y otros datos
                        tvNombreUsuario.text = "Hola, $nombreUsuario!"
                        tvEmailUsuario.text = "Email: $emailUsuario"
                        tvTotalPlantillas.text = "Total de plantillas: 0" // Este valor puede ser dinámico dependiendo de tu lógica
                    }
                }
                .addOnFailureListener {
                    // Manejo de error al obtener los datos de Firestore
                    tvNombreUsuario.text = "Error al obtener datos del usuario"
                }
        } else {
            // Si no hay usuario autenticado, redirigimos al inicio de sesión
            val intent = Intent(this, IniciarSesionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Ir a la pantalla de plantillas
        btnPlantillas.setOnClickListener {
            val intent = Intent(this, ActivityPlantillas::class.java)
            startActivity(intent)
        }

        // Cerrar sesión
        btnLogout.setOnClickListener {
            auth.signOut() // Cerrar sesión en Firebase

            // Redirigir a la pantalla de inicio de sesión
            val intent = Intent(this, IniciarSesionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Volver atrás cerrando la view
        btnBack.setOnClickListener {
            finish() // Esto cierra la Activity y vuelve a la anterior
        }
    }
}
