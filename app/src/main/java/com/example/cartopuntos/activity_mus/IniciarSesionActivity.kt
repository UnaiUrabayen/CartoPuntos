package com.example.cartopuntos.activity_mus

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
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
    private lateinit var chkMantenerSesion: CheckBox
    private lateinit var sharedPreferences: SharedPreferences

    private val usuarioService = UsuarioService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.iniciar_sesion)

        edEmailUsuario = findViewById(R.id.ed_EmailUsuario)
        edContraseniaUsuario = findViewById(R.id.ed_ContraseniaUsuario)
        btnIniciarSesion = findViewById(R.id.btn_crearCuenta)
        chkMantenerSesion = findViewById(R.id.bch_mantnersesion)

        sharedPreferences = getSharedPreferences("SesionUsuario", MODE_PRIVATE)

        // Si la sesión ya estaba guardada, saltar directamente a la pantalla de juego
        val sesionActiva = sharedPreferences.getBoolean("mantener_sesion", false)
        if (sesionActiva) {
            startActivity(Intent(this, EscogerJuego::class.java))
            finish()
        }

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
                    // Guardar sesión si el checkbox está marcado
                    if (chkMantenerSesion.isChecked) {
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("mantener_sesion", true)
                        editor.putString("email_guardado", email)
                        editor.apply()
                    }

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
