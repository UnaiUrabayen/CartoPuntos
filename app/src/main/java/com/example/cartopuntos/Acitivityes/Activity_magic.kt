package com.example.cartopuntos.Acitivityes

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cartopuntos.R

class Activity_magic : AppCompatActivity() {

    private val vidaInicial = 40
    private val jugadores = mutableMapOf<Int, Int>() // jugador -> vida actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.view_activity_magic_4) // Cambia esto según el layout activo

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Intenta configurar hasta 8 jugadores, pero ignora los que no están en el layout
        for (jugador in 1..8) {
            configurarJugadorSiExiste(jugador)
        }
    }

    private fun configurarJugadorSiExiste(jugador: Int) {
        val idMas = resources.getIdentifier("btn_mas$jugador", "id", packageName)
        val idMenos = resources.getIdentifier("btn_menos$jugador", "id", packageName)
        val idPuntos = resources.getIdentifier("tv_puntosJuj$jugador", "id", packageName)

        val btnMas = findViewById<Button?>(idMas)
        val btnMenos = findViewById<Button?>(idMenos)
        val tvPuntos = findViewById<TextView?>(idPuntos)

        if (btnMas == null || btnMenos == null || tvPuntos == null) {
            // Este jugador no está en el layout, salimos silenciosamente
            return
        }

        jugadores[jugador] = vidaInicial
        tvPuntos.text = vidaInicial.toString()

        btnMas.setOnClickListener {
            if ((jugadores[jugador] ?: 0) <= 0) return@setOnClickListener
            jugadores[jugador] = jugadores[jugador]!! + 1
            tvPuntos.text = jugadores[jugador].toString()
        }

        btnMenos.setOnClickListener {
            val vidaActual = jugadores[jugador]!!
            if (vidaActual > 1) {
                jugadores[jugador] = vidaActual - 1
                tvPuntos.text = jugadores[jugador].toString()
            } else if (vidaActual == 1) {
                jugadores[jugador] = 0
                tvPuntos.text = "MUERTO"
                btnMas.isEnabled = false
                btnMenos.isEnabled = false
                Toast.makeText(this, "Jugador $jugador ha muerto", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
