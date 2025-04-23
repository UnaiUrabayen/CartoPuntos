package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var sharedPreferences: SharedPreferences

    private val usuarioService = UsuarioService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_iniciar_sesion)

        edEmailUsuario = findViewById(R.id.ed_EmailUsuario)
        edContraseniaUsuario = findViewById(R.id.ed_ContraseniaUsuario)
        btnIniciarSesion = findViewById(R.id.btn_crearCuenta)
        chkMantenerSesion = findViewById(R.id.bch_mantnersesion)

        sharedPreferences = getSharedPreferences("SesionUsuario", MODE_PRIVATE)

        // Auto-login
        if (sharedPreferences.getBoolean("mantener_sesion", false)) {
            startActivity(Intent(this, Activity_escogerJuego::class.java))
            finish()
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

            usuarioService.iniciarSesion(email, contrasena) { success, message, nombreUsuario ->
                runOnUiThread {
                    if (success) {
                        if (chkMantenerSesion.isChecked) {
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("mantener_sesion", true)
                            editor.putString("email_guardado", email)

                            // Si el nombre de usuario es nulo, asignamos "Usuario" por defecto
                            editor.putString("nombre_guardado", nombreUsuario ?: "Usuario")
                            editor.apply()
                        }

                        // Aseguramos que siempre haya un mensaje válido para el Toast
                        val toastMessage = message ?: "Inicio de sesión exitoso."
                        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, Activity_escogerJuego::class.java))
                        finish()
                    } else {
                        // En caso de error, mostramos el mensaje de error
                        val errorMessage = message ?: "Ocurrió un error inesperado. Inténtalo nuevamente."
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
