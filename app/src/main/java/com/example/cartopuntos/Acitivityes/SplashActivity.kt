package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establece el contenido de la vista de la splash
        setContentView(R.layout.view_splash)

        // Agregar un retraso para la splash (por ejemplo, 3 segundos)
        Handler().postDelayed({
            // Redirige a IniciarSesionActivity
            val intent = Intent(this, IniciarSesionActivity::class.java)
            startActivity(intent)
            finish() // Termina SplashActivity para que no vuelva atrás
        }, 3000) // 3 segundos
    }
}
