package com.example.cartopuntos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.Activities.CrearCuentaActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Iniciar directamente la actividad CrearCuentaActivity
        val intent = Intent(this, CrearCuentaActivity::class.java)
        startActivity(intent)

        // Opcional: cerrar esta activity si no la necesitas
        finish()
    }
}
