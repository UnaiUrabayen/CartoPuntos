package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.Model.Service.UsuarioService
import com.example.cartopuntos.R

class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var usuarioService: UsuarioService
    private lateinit var edNombreUsuario: EditText
    private lateinit var edEmailUsuario: EditText
    private lateinit var edContraseniaUsuario: EditText
    private lateinit var btnCrearCuenta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_crear_cuenta)

        // Inicializar las vistas
        usuarioService = UsuarioService()
        edNombreUsuario = findViewById(R.id.ed_NombreUsuario)
        edEmailUsuario = findViewById(R.id.ed_EmailUsuario)
        edContraseniaUsuario = findViewById(R.id.ed_ContraseniaUsuario)
        btnCrearCuenta = findViewById(R.id.btn_crearCuenta)

        // Configurar botón de volver (ImageView)
        val imbVolver = findViewById<ImageView>(R.id.imb_vovler)
        imbVolver.setOnClickListener {
            finish() // Cierra esta activity y vuelve a la anterior (Iniciar sesión)
        }

        // Acción al hacer clic en el botón de crear cuenta
        btnCrearCuenta.setOnClickListener {
            val nombreUsuario = edNombreUsuario.text.toString().trim()
            val email = edEmailUsuario.text.toString().trim()
            val contrasena = edContraseniaUsuario.text.toString().trim()

            // Validar campos vacíos
            if (nombreUsuario.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación de formato de correo
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, ingresa un correo válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación de la contraseña
            if (contrasena.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamar al servicio para crear la cuenta
            usuarioService.crearUsuario(nombreUsuario, email, contrasena) { success, message ->
                if (success) {
                    Toast.makeText(this, "Cuenta creada exitosamente.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Activity_escogerJuego::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
