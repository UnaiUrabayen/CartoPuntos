package com.example.cartopuntos.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.Model.Service.UsuarioService
import com.example.cartopuntos.R
import android.util.Patterns

class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var usuarioService: UsuarioService
    private lateinit var edNombreUsuario: EditText
    private lateinit var edEmailUsuario: EditText
    private lateinit var edContraseniaUsuario: EditText
    private lateinit var btnCrearCuenta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crear_cuenta)

        // Inicializar las vistas
        usuarioService = UsuarioService()
        edNombreUsuario = findViewById(R.id.ed_NombreUsuario)
        edEmailUsuario = findViewById(R.id.ed_EmailUsuario)
        edContraseniaUsuario = findViewById(R.id.ed_ContraseniaUsuario)
        btnCrearCuenta = findViewById(R.id.btn_crearCuenta)

        // Acción al hacer clic en el botón
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

            // Validación de la contraseña (opcional, puedes añadir más reglas según sea necesario)
            if (contrasena.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamar al servicio para crear la cuenta
            usuarioService.crearUsuario(nombreUsuario, email, contrasena) { success, message ->
                if (success) {
                    Toast.makeText(this, "Cuenta creada exitosamente.", Toast.LENGTH_SHORT).show()
                    // Aquí puedes realizar la navegación a la siguiente actividad
                    // Por ejemplo:
                     val intent = Intent(this, EscogerJuego::class.java)
                     startActivity(intent)
                     finish()
                } else {
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
