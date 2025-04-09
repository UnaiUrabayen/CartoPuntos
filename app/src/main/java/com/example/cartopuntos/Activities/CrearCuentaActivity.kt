package com.example.cartopuntos.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
        setContentView(R.layout.crear_cuenta)

        usuarioService = UsuarioService()
        edNombreUsuario = findViewById(R.id.ed_NombreUsuario)
        edEmailUsuario = findViewById(R.id.ed_EmailUsuario)
        edContraseniaUsuario = findViewById(R.id.ed_ContraseniaUsuario)
        btnCrearCuenta = findViewById(R.id.btn_crearCuenta)

        btnCrearCuenta.setOnClickListener {
            val nombreUsuario = edNombreUsuario.text.toString().trim()
            val email = edEmailUsuario.text.toString().trim()
            val contrasena = edContraseniaUsuario.text.toString().trim()

            if (nombreUsuario.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            usuarioService.crearUsuario(nombreUsuario, email, contrasena) { success, message ->
                if (success) {
                    Toast.makeText(this, "Cuenta creada exitosamente.", Toast.LENGTH_SHORT).show()
                    // Navegar a la siguiente pantalla o actividad
                } else {
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
