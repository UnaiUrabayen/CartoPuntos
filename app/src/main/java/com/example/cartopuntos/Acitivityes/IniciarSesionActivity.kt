package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.Model.Service.UsuarioService
import com.example.cartopuntos.R

class IniciarSesionActivity : AppCompatActivity() {

    private lateinit var edEmailUsuario: EditText
    private lateinit var edContraseniaUsuario: EditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var chkMantenerSesion: CheckBox

    private val usuarioService = UsuarioService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_iniciar_sesion)

        edEmailUsuario = findViewById(R.id.ed_EmailUsuario)
        edContraseniaUsuario = findViewById(R.id.ed_ContraseniaUsuario)
        btnIniciarSesion = findViewById(R.id.btn_crearCuenta)
        chkMantenerSesion = findViewById(R.id.bch_mantnersesion)

        // Auto-login: Verificamos si hay un usuario autenticado
        usuarioService.obtenerUsuarioActual { user ->
            if (user != null) {
                // Si ya hay un usuario autenticado, redirigimos a la siguiente actividad
                startActivity(Intent(this, Activity_escogerJuego::class.java))
                finish()
            }
        }

        findViewById<TextView>(R.id.tv_crearCuenta).setOnClickListener {
            startActivity(Intent(this, CrearCuentaActivity::class.java))
        }

        btnIniciarSesion.setOnClickListener {
            val email = edEmailUsuario.text.toString().trim()
            val contrasena = edContraseniaUsuario.text.toString().trim()

            if (email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamar a la función de iniciar sesión en el servicio
            usuarioService.iniciarSesion(email, contrasena) { success, message, nombreUsuario ->
                runOnUiThread {
                    if (success) {
                        // Si el inicio de sesión es exitoso, redirigimos a la siguiente actividad
                        Toast.makeText(this, message ?: "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Activity_escogerJuego::class.java))
                        finish()
                    } else {
                        // Si hay un error, mostramos el mensaje de error
                        val errorMessage = message ?: "Ocurrió un error inesperado. Inténtalo nuevamente."
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
