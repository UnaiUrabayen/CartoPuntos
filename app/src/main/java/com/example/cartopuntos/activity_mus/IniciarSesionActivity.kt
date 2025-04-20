package com.example.cartopuntos.activity_mus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.Model.Service.UsuarioService
import com.example.cartopuntos.R

class IniciarSesionActivity : AppCompatActivity() {

    private lateinit var edEmailUsuario: EditText
    private lateinit var edContraseniaUsuario: EditText
    private lateinit var btnIniciarSesion: Button

    private val usuarioService = UsuarioService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.iniciar_sesion)

        edEmailUsuario = findViewById(R.id.ed_EmailUsuario)
        edContraseniaUsuario = findViewById(R.id.ed_ContraseniaUsuario)
        btnIniciarSesion = findViewById(R.id.btn_crearCuenta)

        val tvCrearCuenta = findViewById<TextView>(R.id.tv_crearCuenta)
        tvCrearCuenta.setOnClickListener {
            val intent = Intent(this, CrearCuentaActivity::class.java)
            startActivity(intent)
        }

        btnIniciarSesion.setOnClickListener {
            val email = edEmailUsuario.text.toString().trim()
            val contrasena = edContraseniaUsuario.text.toString().trim()

            if (email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            usuarioService.iniciarSesion(email, contrasena) { success, message ->
                if (success) {
                    Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                     startActivity(Intent(this, EscogerJuego::class.java))
                     finish()
                } else {
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
