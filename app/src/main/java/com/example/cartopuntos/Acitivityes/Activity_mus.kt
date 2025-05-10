package com.example.cartopuntos.Acitivityes

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.cartopuntos.Utils.AjustesDialogFragment
import com.example.cartopuntos.Utils.MenuDialogReiniciarMus
import com.example.cartopuntos.Model.Entity.PartidaMus
import com.example.cartopuntos.Model.Entity.JugadorMus
import com.example.cartopuntos.Model.Entity.PlantillaPerfil
import com.example.cartopuntos.Model.Service.ObtnerPlantilla
import com.example.cartopuntos.R
import com.example.cartopuntos.activities.ActivityPlantillas
import java.util.UUID
import com.bumptech.glide.request.transition.Transition


class Activity_mus : AppCompatActivity() {

    private lateinit var partida: PartidaMus
    private lateinit var menuBTN: ImageView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_mus)

        // Inicializar la partida con jugadores y plantillas
        val plantilla1 = PlantillaPerfil(idPlantilla = "plantilla1", uidUsuario = "uid123", nombreJugador = "Jugador 1", fotoPerfilUrl = "url1", urlFondo = "fondo1")
        val plantilla2 = PlantillaPerfil(idPlantilla = "plantilla2", uidUsuario = "uid456", nombreJugador = "Jugador 2", fotoPerfilUrl = "url2", urlFondo = "fondo2")
        val plantilla3 = PlantillaPerfil(idPlantilla = "plantilla3", uidUsuario = "uid789", nombreJugador = "Jugador 3", fotoPerfilUrl = "url3", urlFondo = "fondo3")
        val plantilla4 = PlantillaPerfil(idPlantilla = "plantilla4", uidUsuario = "uid101", nombreJugador = "Jugador 4", fotoPerfilUrl = "url4", urlFondo = "fondo4")

        val jugador1 = JugadorMus(idJugador = "1", nombreJugador = "Jugador 1", uidUsuario = "uid123", plantilla = plantilla1)
        val jugador2 = JugadorMus(idJugador = "2", nombreJugador = "Jugador 2", uidUsuario = "uid456", plantilla = plantilla2)
        val jugador3 = JugadorMus(idJugador = "3", nombreJugador = "Jugador 3", uidUsuario = "uid789", plantilla = plantilla3)
        val jugador4 = JugadorMus(idJugador = "4", nombreJugador = "Jugador 4", uidUsuario = "uid101", plantilla = plantilla4)

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
            cantidadRondas = 3,
            jugadores = listOf(jugador1, jugador2, jugador3, jugador4)
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
                    mostrarDialogoDeclararGanador()
                }
            )
            menuDialog.show(supportFragmentManager, "MenuDialog")
        }

        menuBTN = findViewById(R.id.imageView_usuario1)
        menuBTN.setOnClickListener {
            val ajustesDialogFragment = AjustesDialogFragment { cantidadRondas ->
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
        val cuadrante1 = findViewById<LinearLayout>(R.id.cuadrante1)
        val cuadrante2 = findViewById<LinearLayout>(R.id.cuadrante2)
        val cuadrante3 = findViewById<LinearLayout>(R.id.cuadrante3)
        val cuadrante4 = findViewById<LinearLayout>(R.id.cuadrante4)


        // Configuración de los íconos para seleccionar plantillas
        val iconJugador1 = findViewById<ImageView>(R.id.icon_plantilla_jug1)
        val iconJugador2 = findViewById<ImageView>(R.id.icon_plantilla_jug2)
        val iconJugador3 = findViewById<ImageView>(R.id.icon_plantilla_jug3)
        val iconJugador4 = findViewById<ImageView>(R.id.icon_plantilla_jug4)

        iconJugador1.setOnClickListener { openPlantillasAdapter(1) }
        iconJugador2.setOnClickListener { openPlantillasAdapter(2) }
        iconJugador3.setOnClickListener { openPlantillasAdapter(3) }
        iconJugador4.setOnClickListener { openPlantillasAdapter(4) }






    }

    private var jugadorSeleccionado = 0

    private fun openPlantillasAdapter(jugadorId: Int) {
        jugadorSeleccionado = jugadorId
        val intent = Intent(this, ActivityPlantillas::class.java)
        intent.putExtra("ORIGEN", "juego")
        startActivityForResult(intent, 100)  // Códigos tipo 100 son arbitrarios
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val plantilla = data?.getSerializableExtra("PLANTILLA") as? PlantillaPerfil
            plantilla?.let {
                partida.jugadores[jugadorSeleccionado - 1].plantilla = it
                actualizarVistaJugador(jugadorSeleccionado, it)
            }
        }

}
    private fun actualizarVistaJugador(numero: Int, plantilla: PlantillaPerfil) {
        // Cargar imagen de fondo en cuadranteX
        val idFondo = resources.getIdentifier("cuadrante$numero", "id", packageName)
        val cuadrante = findViewById<LinearLayout>(idFondo)

// Establecer el fondo del LinearLayout
        Glide.with(this)
            .load(plantilla.urlFondo)
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    cuadrante.background = resource
                }
            })


        // Cargar ícono de jugador en icon_plantilla_jugX
        val idIcon = resources.getIdentifier("icon_plantilla_jug$numero", "id", packageName)
        val icono = findViewById<ImageView>(idIcon)
        Glide.with(this)
            .load(plantilla.fotoPerfilUrl)
            .circleCrop() // <-- Esto la hace redonda
            .into(icono)


        // Setear nombre del jugador en tv_jugadorX
        val idTV = resources.getIdentifier("tv_jugador$numero", "id", packageName)
        val tvJugador = findViewById<TextView>(idTV)
        tvJugador.text = plantilla.nombreJugador
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
            .setPositiveButton("Aceptar") { _, _ -> }
            .setNegativeButton("Cancelar", null)
            .show()
        reiniciarPartida()
    }

    private fun reiniciarPartida() {
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
            cantidadRondas = partida.cantidadRondas
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
        val ganador = if (partida.puntosEquipo1 > partida.puntosEquipo2) "Equipo 1" else "Equipo 2"
        AlertDialog.Builder(this)
            .setMessage("$ganador ha ganado. ¿Quieres continuar?")
            .setPositiveButton("Aceptar", null)
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
