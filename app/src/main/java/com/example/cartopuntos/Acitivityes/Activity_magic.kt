package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.Dialogs.DialogoComandanteDamage
import com.example.cartopuntos.Dialogs.MenuDialogDados
import com.example.cartopuntos.Logica.JuegoMagic
import com.example.cartopuntos.R
import com.example.cartopuntos.Utils.ConfiguracionMagicDialog
import com.example.cartopuntos.Utils.MenuDialogReiniciarMagic

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
            configurarMenuJugador(jugador, cantidad)
        }

        findViewById<ImageView>(R.id.imageView_usuario1).setOnClickListener {
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

        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            val dialog = MenuDialogDados { caras ->
                val resultado = juego.lanzarDado(caras)
                Toast.makeText(this, "Resultado del D$caras: $resultado", Toast.LENGTH_SHORT).show()
            }
            dialog.show(supportFragmentManager, "MenuDadosDialog")
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

    private fun configurarMenuJugador(jugador: Int, cantidad: Int) {
        val idBtn = resources.getIdentifier("btn_menu_jugador$jugador", "id", packageName)
        val btnMenu = findViewById<Button?>(idBtn)

        btnMenu?.setOnClickListener {
            val dialog = DialogoComandanteDamage(
                jugadorId = jugador,
                cantidadJugadores = cantidad,
                juego = juego
            )
            dialog.show(supportFragmentManager, "DialogoComandanteDamage")
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
        return juego.getJugadores().maxByOrNull { juego.getVida(it.key) ?: 0 }?.key ?: -1
    }
}
