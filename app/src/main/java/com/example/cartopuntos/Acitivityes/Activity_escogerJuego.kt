package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.R

class Activity_escogerJuego : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_escoger_juego) // Asegúrate de que este sea el nombre de tu layout XML

        val btnUsuario: ImageView = findViewById(R.id.imgb_btn_usuario)
        val btnMagic: ImageButton = findViewById(R.id.imb_magic)
        val btnMus: ImageButton = findViewById(R.id.imb_mus)

        btnUsuario.setOnClickListener {
            val intent = Intent(this, Activity_usuario::class.java)
            startActivity(intent)
        }

        btnMagic.setOnClickListener {
            val intent = Intent(this, Activity_magic::class.java)
            startActivity(intent)
        }

        btnMus.setOnClickListener {
            val intent = Intent(this, Activity_mus::class.java)
            startActivity(intent)
        }
    }
}
