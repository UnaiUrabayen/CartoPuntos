package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.Dialogs.MenuDialogReiniciarMagic
import com.example.cartopuntos.Logica.JuegoMagic
import com.example.cartopuntos.R


class Activity_magic : AppCompatActivity() {

    private lateinit var juego: JuegoMagic
    private var sonido = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cantidad = intent.getIntExtra("jugadores", 4)
        sonido = intent.getBooleanExtra("sonido", false)
        val permitirNegativos = intent.getBooleanExtra("vidaNegativa", false)
        val autoReset = intent.getBooleanExtra("autoReset", false)

        val layoutId = obtenerLayoutPorJugadores(cantidad)
        setContentView(layoutId)

        juego = JuegoMagic(
            cantidadJugadores = cantidad,
            permitirVidaNegativa = permitirNegativos,
            autoReinicioActivo = autoReset,
            onJugadorMuere = { jugador -> onJugadorMuerto(jugador) },
            onGanadorDetectado = { ganador -> onGanador(ganador) }
        )

        for (jugador in 1..cantidad) {
            configurarControlesJugador(jugador)
        }

        findViewById<ImageView>(R.id.menuBTN).setOnClickListener {
            val dialog = ConfiguracionMagicDialog { newCantidad, sonidoNuevo, vidaNeg, autoResetNuevo ->
                val intent = Intent(this, Activity_magic::class.java).apply {
                    putExtra("jugadores", newCantidad)
                    putExtra("sonido", sonidoNuevo)
                    putExtra("vidaNegativa", vidaNeg)
                    putExtra("autoReset", autoResetNuevo)
                }
                finish()
                startActivity(intent)
            }
            dialog.show(supportFragmentManager, "ConfigDialog")
        }
        findViewById<ImageView>(R.id.reloadBTN).setOnClickListener {
            val nombresJugadores = (1..cantidad).map { "Jugador $it" }

            val dialog = MenuDialogReiniciarMagic(
                jugadores = nombresJugadores,
                onReiniciarClick = {
                    reiniciarUI()
                },
                onDeclararClick = { ganador ->
                    Toast.makeText(this, "El ganador es: $ganador", Toast.LENGTH_SHORT).show()
                }
            )
            dialog.show(supportFragmentManager, "ReiniciarDialog")
        }




    }

    private fun configurarControlesJugador(jugador: Int) {
        val idMas = resources.getIdentifier("btn_mas$jugador", "id", packageName)
        val idMenos = resources.getIdentifier("btn_menos$jugador", "id", packageName)
        val idPuntos = resources.getIdentifier("tv_puntosJuj$jugador", "id", packageName)

        val btnMas = findViewById<Button>(idMas)
        val btnMenos = findViewById<Button>(idMenos)
        val tvPuntos = findViewById<TextView>(idPuntos)

        tvPuntos.text = juego.getVida(jugador).toString()

        btnMas.setOnClickListener {
            if (juego.modificarVida(jugador, +1)) {
                tvPuntos.text = juego.getVida(jugador).toString()
            }
        }

        btnMenos.setOnClickListener {
            if (juego.modificarVida(jugador, -1)) {
                val nuevaVida = juego.getVida(jugador)!!
                tvPuntos.text = if (nuevaVida <= 0) "MUERTO" else nuevaVida.toString()
                if (nuevaVida <= 0) {
                    btnMas.isEnabled = false
                    btnMenos.isEnabled = false
                }
            }
        }
    }

    private fun onJugadorMuerto(jugador: Int) {
        if (sonido) {
            val mediaPlayer = MediaPlayer.create(this, R.raw.death_sound)
            mediaPlayer.start()
        }
        Toast.makeText(this, "Jugador $jugador ha muerto", Toast.LENGTH_SHORT).show()
    }

    private fun onGanador(ganador: Int) {
        Toast.makeText(this, "Jugador $ganador ha ganado", Toast.LENGTH_SHORT).show()
        reiniciarUI()
    }

    private fun reiniciarUI() {
        juego.reiniciar()
        for ((jugador, _) in juego.getJugadores()) {
            val tv = findViewById<TextView>(
                resources.getIdentifier("tv_puntosJuj$jugador", "id", packageName)
            )
            val btnMas = findViewById<Button>(
                resources.getIdentifier("btn_mas$jugador", "id", packageName)
            )
            val btnMenos = findViewById<Button>(
                resources.getIdentifier("btn_menos$jugador", "id", packageName)
            )

            tv?.text = juego.getVida(jugador).toString()
            btnMas?.isEnabled = true
            btnMenos?.isEnabled = true
        }
    }

    private fun obtenerLayoutPorJugadores(cantidad: Int): Int {
        return when (cantidad) {
            2 -> R.layout.view_activity_magic_2
            3 -> R.layout.view_activity_magic_3
            4 -> R.layout.view_activity_magic_4
            6 -> R.layout.view_activity_magic_6
            8 -> R.layout.view_activity_magic_8
            else -> R.layout.view_activity_magic_4
        }
    }
    private fun detectarGanador(): Int {
        // Lógica para determinar al ganador, podrías basarlo en las vidas restantes
        val ganador = juego.getJugadores().maxByOrNull { juego.getVida(it.key)!! }?.key
        return ganador ?: -1  // Devuelve el ID del ganador, o -1 si no hay ganador
    }

}
