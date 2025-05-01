package com.example.cartopuntos.Acitivityes

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.R

class Activity_mus : AppCompatActivity() {

    // Definir las variables para los puntos de los jugadores
    private var puntosJugador1 = 0
    private var puntosJugador2 = 0
    private var puntosJugador3 = 0
    private var puntosJugador4 = 0

    // Variables para los puntos de partida de cada equipo
    private var puntosPartidaEquipo1 = 0
    private var puntosPartidaEquipo2 = 0

    // Variables para contar las victorias de los equipos
    private var victoriasEquipo1 = 0
    private var victoriasEquipo2 = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_mus)

        // Obtener referencias a las vistas
        val tvPuntosJugador1 = findViewById<TextView>(R.id.tv_puntosJuj1)
        val tvPuntosJugador2 = findViewById<TextView>(R.id.tv_puntosJuj2)
        val tvPuntosJugador3 = findViewById<TextView>(R.id.tv_puntosJuj3)
        val tvPuntosJugador4 = findViewById<TextView>(R.id.tv_puntosJuj4)

        val tvPuntosPartidaEquipo1 = findViewById<TextView>(R.id.contadorPunto1)  // Equipo 1
        val tvPuntosPartidaEquipo2 = findViewById<TextView>(R.id.contadorPunto2)  // Equipo 2

        // Obtener referencias a los botones
        val btnMenosJugador1 = findViewById<Button>(R.id.btn_menos)
        val btnMasJugador1 = findViewById<Button>(R.id.btn_mas)

        val btnMenosJugador2 = findViewById<Button>(R.id.btn_menos2)
        val btnMasJugador2 = findViewById<Button>(R.id.btn_mas2)

        val btnMenosJugador3 = findViewById<Button>(R.id.btn_menos3)
        val btnMasJugador3 = findViewById<Button>(R.id.btn_mas3)

        val btnMenosJugador4 = findViewById<Button>(R.id.btn_menos4)
        val btnMasJugador4 = findViewById<Button>(R.id.btn_mas4)

        resetearContadores()

        // Configurar el comportamiento de los botones de Jugador 1 (Equipo 1) - Solo suma puntos
        btnMasJugador1.setOnClickListener {
            puntosJugador1++
            tvPuntosJugador1.text = "$puntosJugador1 T"
            controlarPuntoJugador1()
            verificarPuntoPartida()
        }

        btnMenosJugador1.setOnClickListener {
            if (puntosJugador1 > 0) {
                puntosJugador1--
                tvPuntosJugador1.text = "$puntosJugador1 T"
            }
        }

        // Configurar el comportamiento de los botones de Jugador 2 (Equipo 2) - Solo suma puntos
        btnMasJugador2.setOnClickListener {
            puntosJugador2++
            tvPuntosJugador2.text = "$puntosJugador2 H"
            verificarPuntoPartida()
        }

        btnMenosJugador2.setOnClickListener {
            if (puntosJugador2 > 0) {
                puntosJugador2--
                tvPuntosJugador2.text = "$puntosJugador2 H"
            }
        }

        // Configurar el comportamiento de los botones de Jugador 3 (Equipo 2) - Solo suma puntos
        btnMasJugador3.setOnClickListener {
            puntosJugador3++
            tvPuntosJugador3.text = "$puntosJugador3 T"
            controlarPuntoJugador3()
            verificarPuntoPartida()
        }

        btnMenosJugador3.setOnClickListener {
            if (puntosJugador3 > 0) {
                puntosJugador3--
                tvPuntosJugador3.text = "$puntosJugador3 T"
            }
        }

        // Configurar el comportamiento de los botones de Jugador 4 (Equipo 1) - Solo suma puntos
        btnMasJugador4.setOnClickListener {
            puntosJugador4++
            tvPuntosJugador4.text = "$puntosJugador4 H"
            verificarPuntoPartida()
        }

        btnMenosJugador4.setOnClickListener {
            if (puntosJugador4 > 0) {
                puntosJugador4--
                tvPuntosJugador4.text = "$puntosJugador4 H"
            }
        }
    }

    // Función para controlar los puntos de Jugador 1 (Equipo 1)
    @SuppressLint("SetTextI18n")
    private fun controlarPuntoJugador1() {
        if (puntosJugador1 == 5) {
            puntosJugador1 = 0 // Resetear el contador de puntos del Jugador 1
            puntosJugador4++ // Sumar 1 punto al Jugador 4
            findViewById<TextView>(R.id.tv_puntosJuj4).text = "$puntosJugador4 H"  // Actualizar la vista de Jugador 4
        }
    }

    // Función para controlar los puntos de Jugador 3 (Equipo 2)
    @SuppressLint("SetTextI18n")
    private fun controlarPuntoJugador3() {
        if (puntosJugador3 == 5) {
            puntosJugador3 = 0 // Resetear el contador de puntos del Jugador 3
            puntosJugador2++ // Sumar 1 punto al Jugador 2
            findViewById<TextView>(R.id.tv_puntosJuj2).text = "$puntosJugador2 H"  // Actualizar la vista de Jugador 2
        }
    }

    // Función para verificar si un equipo gana un punto de partida cuando llega a 8 puntos
    private fun verificarPuntoPartida() {
        // Verificar si el Jugador 4 (Equipo 1) llega a 8 puntos
        if (puntosJugador4 == 8) {
            puntosPartidaEquipo1++
            findViewById<TextView>(R.id.contadorPunto1).text = "$puntosPartidaEquipo1"  // Actualizar el contador del Equipo 1
            resetearContadores()  // Reiniciar contadores
        }

        // Verificar si el Jugador 2 (Equipo 2) llega a 8 puntos
        if (puntosJugador2 == 8) {
            puntosPartidaEquipo2++
            findViewById<TextView>(R.id.contadorPunto2).text = "$puntosPartidaEquipo2"  // Actualizar el contador del Equipo 2
            resetearContadores()  // Reiniciar contadores
        }

        // Verificar si un equipo ha ganado 3 partidas
        if (puntosPartidaEquipo1 == 3) {
            mostrarMensajeVictoria("Equipo 1")
        }

        if (puntosPartidaEquipo2 == 3) {
            mostrarMensajeVictoria("Equipo 2")
        }
    }

    // Función para mostrar el mensaje de victoria
    private fun mostrarMensajeVictoria(equipo: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("$equipo ha ganado el juego. ¿Quieres guardar la partida?")
            .setCancelable(false)
            .setPositiveButton("Aceptar") { _, _ ->
                // Acción para guardar la partida (no implementada aún)
            }
            .setNegativeButton("Cancelar") { _, _ ->
                // Acción para cancelar (no implementada aún)
            }

        val alert = builder.create()
        alert.show()

        reiniciarPartida()
    }

    // Función para reiniciar los contadores después de que un equipo gane una partida
    private fun reiniciarPartida() {
        // Reiniciar los puntos de los jugadores al inicio
        puntosJugador1 = 0
        puntosJugador2 = 0
        puntosJugador3 = 0
        puntosJugador4 = 0
        puntosPartidaEquipo1 = 0
        puntosPartidaEquipo2 = 0

        // Actualizar la vista de los jugadores y los equipos
        findViewById<TextView>(R.id.tv_puntosJuj1).text = "0 T"
        findViewById<TextView>(R.id.tv_puntosJuj2).text = "0 H"
        findViewById<TextView>(R.id.tv_puntosJuj3).text = "0 T"
        findViewById<TextView>(R.id.tv_puntosJuj4).text = "0 H"
        findViewById<TextView>(R.id.contadorPunto1).text = "0"
        findViewById<TextView>(R.id.contadorPunto2).text = "0"
    }

    // Función para reiniciar los contadores antes de cada ronda
    private fun resetearContadores() {
        // Se hace el reset de los puntos solo cuando se termine una partida
        // No se reinician automáticamente con cada acción de punto.
        puntosJugador1 = 0
        puntosJugador2 = 0
        puntosJugador3 = 0
        puntosJugador4 = 0


        // Actualizar la vista de los jugadores y los equipos
        findViewById<TextView>(R.id.tv_puntosJuj1).text = "0 T"
        findViewById<TextView>(R.id.tv_puntosJuj2).text = "0 H"
        findViewById<TextView>(R.id.tv_puntosJuj3).text = "0 T"
        findViewById<TextView>(R.id.tv_puntosJuj4).text = "0 H"
    }
}
