package com.example.cartopuntos.Acitivityes


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cartopuntos.Utils.AjustesDialogFragment
import com.example.cartopuntos.Utils.MenuDialogReiniciarMus
import com.example.cartopuntos.Model.Entity.PartidaMus
import com.example.cartopuntos.R

import kotlinx.coroutines.launch
import java.util.UUID

class Activity_mus : AppCompatActivity() {

    private lateinit var partida: PartidaMus
    private lateinit var menuBTN: ImageView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_mus)

        partida = PartidaMus(
            id = UUID.randomUUID().toString(),
            fecha = System.currentTimeMillis(),
            puntosJugador1 = 0,
            puntosJugador2 = 0,
            puntosJugador3 = 0,
            puntosJugador4 = 0,
            puntosEquipo1 = 0,
            puntosEquipo2 = 0,
            victoriasEquipo1 = 0,
            victoriasEquipo2 = 0,
            equipoGanador = null,
            nombrePartida = "Partida X",
            cantidadRondas = 3 // Valor por defecto
        )

        val tvJ1 = findViewById<TextView>(R.id.tv_puntosJuj1)
        val tvJ2 = findViewById<TextView>(R.id.tv_puntosJuj2)
        val tvJ3 = findViewById<TextView>(R.id.tv_puntosJuj3)
        val tvJ4 = findViewById<TextView>(R.id.tv_puntosJuj4)
        val tvEq1 = findViewById<TextView>(R.id.contadorPunto1)
        val tvEq2 = findViewById<TextView>(R.id.contadorPunto2)

        val btnMas1 = findViewById<Button>(R.id.btn_mas1)
        val btnMenos1 = findViewById<Button>(R.id.btn_menos1)
        val btnMas2 = findViewById<Button>(R.id.btn_mas2)
        val btnMenos2 = findViewById<Button>(R.id.btn_menos2)
        val btnMas3 = findViewById<Button>(R.id.btn_mas3)
        val btnMenos3 = findViewById<Button>(R.id.btn_menos3)
        val btnMas4 = findViewById<Button>(R.id.btn_mas4)
        val btnMenos4 = findViewById<Button>(R.id.btn_menos4)


        val reloadButton = findViewById<ImageView>(R.id.reloadBTN)
        reloadButton.setOnClickListener {
            val menuDialog = MenuDialogReiniciarMus(
                onReiniciarClick = { reiniciarPartida() },
                onDeclararClick = {
                    // Mostrar el diálogo para declarar el ganador
                    mostrarDialogoDeclararGanador()
                }
            )
            menuDialog.show(supportFragmentManager, "MenuDialog")
        }

        // Referencia al ImageView menuBTN
        menuBTN = findViewById(R.id.imageView_usuario1)

        // Configurar el click para abrir el diálogo de ajustes de rondas
        menuBTN.setOnClickListener {
            // Mostrar el nuevo DialogFragment para ajustes de rondas
            val ajustesDialogFragment = AjustesDialogFragment { cantidadRondas ->
                // Aquí recibimos el valor de rondas seleccionado
                partida.cantidadRondas = cantidadRondas
                Toast.makeText(this, "Rondas cambiadas a $cantidadRondas", Toast.LENGTH_SHORT).show()
            }
            ajustesDialogFragment.show(supportFragmentManager, "AjustesDialog")
        }

        resetearContadores()

        btnMas1.setOnClickListener {
            partida.puntosJugador1++
            tvJ1.text = "${partida.puntosJugador1} T"
            controlarPuntoJugador1(tvJ4)
            verificarPuntoPartida(tvEq1, tvEq2, tvJ1, tvJ2, tvJ3, tvJ4)
        }

        btnMenos1.setOnClickListener {
            if (partida.puntosJugador1 > 0) {
                partida.puntosJugador1--
                tvJ1.text = "${partida.puntosJugador1} T"
            }
        }

        btnMas2.setOnClickListener {
            partida.puntosJugador2++
            tvJ2.text = "${partida.puntosJugador2} H"
            verificarPuntoPartida(tvEq1, tvEq2, tvJ1, tvJ2, tvJ3, tvJ4)
        }

        btnMenos2.setOnClickListener {
            if (partida.puntosJugador2 > 0) {
                partida.puntosJugador2--
                tvJ2.text = "${partida.puntosJugador2} H"
            }
        }

        btnMas3.setOnClickListener {
            partida.puntosJugador3++
            tvJ3.text = "${partida.puntosJugador3} T"
            controlarPuntoJugador3(tvJ2)
            verificarPuntoPartida(tvEq1, tvEq2, tvJ1, tvJ2, tvJ3, tvJ4)
        }

        btnMenos3.setOnClickListener {
            if (partida.puntosJugador3 > 0) {
                partida.puntosJugador3--
                tvJ3.text = "${partida.puntosJugador3} T"
            }
        }

        btnMas4.setOnClickListener {
            partida.puntosJugador4++
            tvJ4.text = "${partida.puntosJugador4} H"
            verificarPuntoPartida(tvEq1, tvEq2, tvJ1, tvJ2, tvJ3, tvJ4)
        }

        btnMenos4.setOnClickListener {
            if (partida.puntosJugador4 > 0) {
                partida.puntosJugador4--
                tvJ4.text = "${partida.puntosJugador4} H"
            }
        }
    }

    private fun controlarPuntoJugador1(tvJ4: TextView) {
        if (partida.puntosJugador1 == 5) {
            partida.puntosJugador1 = 0
            partida.puntosJugador4++
            tvJ4.text = "${partida.puntosJugador4} H"
        }
    }

    private fun controlarPuntoJugador3(tvJ2: TextView) {
        if (partida.puntosJugador3 == 5) {
            partida.puntosJugador3 = 0
            partida.puntosJugador2++
            tvJ2.text = "${partida.puntosJugador2} H"
        }
    }

    private fun verificarPuntoPartida(
        tvEq1: TextView,
        tvEq2: TextView,
        tvJ1: TextView,
        tvJ2: TextView,
        tvJ3: TextView,
        tvJ4: TextView
    ) {
        if (partida.puntosJugador4 == 8) {
            partida.puntosEquipo1++
            tvEq1.text = "${partida.puntosEquipo1}"
            resetearContadores()
        }

        if (partida.puntosJugador2 == 8) {
            partida.puntosEquipo2++
            tvEq2.text = "${partida.puntosEquipo2}"
            resetearContadores()
        }

        if (partida.puntosEquipo1 == partida.cantidadRondas) {
            partida.equipoGanador = "Equipo 1"
            partida.victoriasEquipo1++
            mostrarMensajeVictoria("Equipo 1")
        }

        if (partida.puntosEquipo2 == partida.cantidadRondas) {
            partida.equipoGanador = "Equipo 2"
            partida.victoriasEquipo2++
            mostrarMensajeVictoria("Equipo 2")
        }

        actualizarVista(tvJ1, tvJ2, tvJ3, tvJ4)
    }

    private fun mostrarMensajeVictoria(equipo: String) {
        AlertDialog.Builder(this)
            .setMessage("$equipo ha ganado el juego. ¿Quieres guardar la partida?")
            .setCancelable(false)
            .setPositiveButton("Aceptar") { _, _ ->
                // Guardar la partida en la base de datos de manera asíncrona
                lifecycleScope.launch {

                }
            }
            .setNegativeButton("Cancelar", null)
            .show()

        reiniciarPartida()
    }

    private fun reiniciarPartida() {
        partida = PartidaMus(
            id = UUID.randomUUID().toString(), // Genera un nuevo ID único para la nueva partida
            fecha = System.currentTimeMillis(),
            puntosJugador1 = 0,
            puntosJugador2 = 0,
            puntosJugador3 = 0,
            puntosJugador4 = 0,
            puntosEquipo1 = 0,
            puntosEquipo2 = 0,
            victoriasEquipo1 = 0,
            victoriasEquipo2 = 0,
            equipoGanador = null,
            nombrePartida = "Partida X",
            cantidadRondas =partida.cantidadRondas //guardar ajuste

        )
        resetearContadores()
        findViewById<TextView>(R.id.contadorPunto1).text = "0"
        findViewById<TextView>(R.id.contadorPunto2).text = "0"
    }

    private fun resetearContadores() {
        partida.puntosJugador1 = 0
        partida.puntosJugador2 = 0
        partida.puntosJugador3 = 0
        partida.puntosJugador4 = 0

        findViewById<TextView>(R.id.tv_puntosJuj1).text = "0 T"
        findViewById<TextView>(R.id.tv_puntosJuj2).text = "0 H"
        findViewById<TextView>(R.id.tv_puntosJuj3).text = "0 T"
        findViewById<TextView>(R.id.tv_puntosJuj4).text = "0 H"
    }

    private fun actualizarVista(tvJ1: TextView, tvJ2: TextView, tvJ3: TextView, tvJ4: TextView) {
        tvJ1.text = "${partida.puntosJugador1} T"
        tvJ2.text = "${partida.puntosJugador2} H"
        tvJ3.text = "${partida.puntosJugador3} T"
        tvJ4.text = "${partida.puntosJugador4} H"
    }
    private fun mostrarDialogoDeclararGanador() {
        val opcionesGanador = arrayOf("Equipo 1", "Equipo 2", "Cancelar")

        // Crear el diálogo con las opciones
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Quién ha ganado?")
        builder.setItems(opcionesGanador) { dialog, which ->
            when (which) {
                0 -> {
                    // Equipo 1 ganó
                    partida.equipoGanador = "Equipo 1"
                    partida.victoriasEquipo1++
                    mostrarMensajeVictoria("Equipo 1")
                }
                1 -> {
                    // Equipo 2 ganó
                    partida.equipoGanador = "Equipo 2"
                    partida.victoriasEquipo2++
                    mostrarMensajeVictoria("Equipo 2")
                }
                2 -> {
                    // Cancelar, no hace nada
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }


}
