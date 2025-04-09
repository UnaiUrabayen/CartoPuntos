package com.example.cartopuntos.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.Model.Service.UsuarioService
import com.example.cartopuntos.R

class IniciarSesionActivity : AppCompatActivity() {

    private lateinit var edEmailUsuario: EditText
    private lateinit var edContraseniaUsuario: EditText
    private lateinit var btnIniciarSesion: Button

    private val usuarioService = UsuarioService()  // Crear la instancia del servicio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.iniciar_sesion)  // Asegúrate de que este archivo de layout exista

        // Inicializar las vistas
        edEmailUsuario = findViewById(R.id.ed_EmailUsuario)
        edContraseniaUsuario = findViewById(R.id.ed_ContraseniaUsuario)
        btnIniciarSesion = findViewById(R.id.btn_crearCuenta)  // Cambia el ID si es necesario

        // Establecer el comportamiento del botón al hacer clic
        btnIniciarSesion.setOnClickListener {
            val email = edEmailUsuario.text.toString().trim()
            val contrasena = edContraseniaUsuario.text.toString().trim()

            // Validación de campos
            if (email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamar al servicio para realizar el inicio de sesión
            usuarioService.iniciarSesion(email, contrasena) { success, message ->
                if (success) {
                    Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                    // Navegar a la siguiente actividad (por ejemplo: pantalla principal)
                    // startActivity(Intent(this, MainActivity::class.java))
                    // finish() // Finalizar esta actividad
                } else {
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
