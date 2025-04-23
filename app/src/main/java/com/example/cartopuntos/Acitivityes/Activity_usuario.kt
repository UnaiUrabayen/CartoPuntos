package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.R

class Activity_usuario : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvEmailUsuario: TextView
    private lateinit var tvTotalPlantillas: TextView
    private lateinit var btnPlantillas: Button
    private lateinit var btnLogout: ImageView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_usuario)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("SesionUsuario", MODE_PRIVATE)

        // Enlazar vistas
        tvNombreUsuario = findViewById(R.id.tv_nombreUsuario)
        tvEmailUsuario = findViewById(R.id.tv_emailUsuario)
        tvTotalPlantillas = findViewById(R.id.tv_totalPartidas)
        btnPlantillas = findViewById(R.id.btn_plantillas)
        btnLogout = findViewById(R.id.imb_logout)
        btnBack = findViewById(R.id.imb_atras)

        // Obtener datos desde SharedPreferences
        val nombreUsuario = sharedPreferences.getString("nombre_guardado", "Usuario")
        val emailUsuario = sharedPreferences.getString("email_guardado", "user@thx.com")

        // Mostrar nombre, email y contador
        tvNombreUsuario.text = "Hola, $nombreUsuario!"
        tvEmailUsuario.text = "Email: $emailUsuario"
        tvTotalPlantillas.text = "Total de plantillas: 0"

        // Ir a la pantalla de plantillas
        btnPlantillas.setOnClickListener {
            val intent = Intent(this, Activity_plantillas::class.java)
            startActivity(intent)
        }

        // Cerrar sesión
        btnLogout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, IniciarSesionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Volver atrás cerrando la view
        btnBack.setOnClickListener {
            finish() // Esto cierra la Activity y vuelve a la anterior
        }
    }
}
