package com.example.cartopuntos.Acitivityes

import MenuDialogReiniciarMagic
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.cartopuntos.Dialogs.DialogoComandanteDamage
import com.example.cartopuntos.Dialogs.MenuDialogDados

import com.example.cartopuntos.Model.Entity.PlantillaPerfil
import com.example.cartopuntos.R
import com.bumptech.glide.request.transition.Transition
import com.example.cartopuntos.Logica.JuegoMagic
import com.example.cartopuntos.Model.Adapter.PartidaAdapter
import com.example.cartopuntos.Model.Service.MagicService

import com.example.cartopuntos.Utils.ConfiguracionMagicDialog
import com.example.cartopuntos.activities.ActivityPlantillas

class Activity_magic : AppCompatActivity() {

    private var sonido = false
    private var juegoMagicAdapter: PartidaAdapter? = null
    private lateinit var juego: JuegoMagic  // Agregar la propiedad de la juego
    private val magicService = MagicService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idPartida = intent.getStringExtra("id_partida")
        Log.d("ActivityUsuario", "ID de juego recibido: $idPartida")

        if (idPartida != null) {
            // Intentamos obtener la juego por ID
            magicService.obtenerPartidaPorId(
                idPartida,
                onSuccess = { juegoObtenida ->
                    juego = juegoObtenida  // Se convierte la juego obtenida en la actual
                    actualizarVistaPartida()  // Actualiza la vista con los datos de la juego
                },
                onFailure = {
                    Toast.makeText(this, "No se encontró la juego con ID: $idPartida", Toast.LENGTH_SHORT).show()
                    crearNuevaPartida()  // Si no se encuentra la juego, crear una nueva
                }
            )
        } else {
            // Si no hay ID, se crea una nueva juego
            crearNuevaPartida()
        }
    }

    private fun crearNuevaPartida() {
        val cantidad = intent.getIntExtra("jugadores", 4)
        sonido = intent.getBooleanExtra("sonido", false)
        val permitirNegativos = intent.getBooleanExtra("vidaNegativa", false)
        val autoReset = intent.getBooleanExtra("autoReset", false)

        val layoutId = obtenerLayoutPorJugadores(cantidad)
        setContentView(layoutId)

        // Crear la nueva juego
        juego = JuegoMagic(
            cantidadJugadores = cantidad,
            permitirVidaNegativa = permitirNegativos,
            autoReinicioActivo = autoReset
        )

        // Asignar las lambdas después de la creación del objeto
        juego.onJugadorMuere = { jugador -> onJugadorMuerto(jugador) }
        juego.onGanadorDetectado = { ganador -> onGanador(ganador) }

        // Configurar los controles de los jugadores
        for (jugador in 1..cantidad) {
            configurarControlesJugador(jugador)
            configurarMenuJugador(jugador, cantidad)
            val iconId = resources.getIdentifier("icon_plantilla_jug$jugador", "id", packageName)
            val icono = findViewById<ImageView>(iconId)
            icono?.setOnClickListener { openPlantillasAdapter(jugador) }
        }

        // Configuración de otros controles UI como reiniciar y lanzar dados
        configurarControlesUI()
    }

    private fun actualizarVistaPartida() {
        // Aquí actualizas la UI de acuerdo con la juego cargada
        for (jugador in 1..juego.cantidadJugadores) {
            configurarControlesJugador(jugador)
            configurarMenuJugador(jugador, juego.cantidadJugadores)
            val iconId = resources.getIdentifier("icon_plantilla_jug$jugador", "id", packageName)
            val icono = findViewById<ImageView>(iconId)
            icono?.setOnClickListener { openPlantillasAdapter(jugador) }
        }
        // Aquí también puedes cargar otros datos de la juego, como las vidas, los jugadores, etc.
    }

    private fun configurarControlesUI() {
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
            val nombresJugadores = (1..juego.cantidadJugadores).map { "Jugador $it" }

            val dialog = MenuDialogReiniciarMagic(
                jugadores = nombresJugadores,
                partida = juego,
                onReiniciarClick = { reiniciarUI() }
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




    private var jugadorSeleccionado = 0

    private fun openPlantillasAdapter(jugadorId: Int) {
        jugadorSeleccionado = jugadorId
        val intent = Intent(this, ActivityPlantillas::class.java)
        intent.putExtra("ORIGEN", "juego")
        startActivityForResult(intent, 100)  // Códigos tipo 100 son arbitrarios
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Comprobar si el código de la solicitud es el que esperamos (100 en este caso)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Verificar si el dato que queremos es un objeto de tipo PlantillaPerfil
            val plantilla = data?.getSerializableExtra("PLANTILLA") as? PlantillaPerfil
            plantilla?.let {
                // Actualizar la plantilla del jugador seleccionado
                juego.jugadores[jugadorSeleccionado - 1]?.plantilla = it

                // Actualizar la vista del jugador con la plantilla recibida
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
            .circleCrop() // Esto hace que el ícono sea redondo
            .into(icono)

        // Setear nombre del jugador en tv_jugadorX
        val idTV = resources.getIdentifier("tv_jugador$numero", "id", packageName)
        val tvJugador = findViewById<TextView>(idTV)
        tvJugador.text = plantilla.nombreJugador
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
                tvPuntos.text = if (nuevaVida <= 0) "\uD83D\uDC80" else nuevaVida.toString()
                if (nuevaVida <= 0) {
                    btnMas.isEnabled = false
                    btnMenos.isEnabled = false

                    // Revisar cuántos jugadores están vivos
                    val vivos = juego.getJugadoresConVidas().filter { it.value > 0 }
                    if (vivos.size == 1) {
                        val ganador = vivos.keys.first()
                        mostrarDialogoGuardarPartida()
                    }
                }
            }
        }
    }

    private fun mostrarDialogoGuardarPartida() {
        val input = EditText(this)
        input.hint = "Nombre de la partida"

        AlertDialog.Builder(this)
            .setTitle("Guardar partida")
            .setMessage("Introduce un nombre para la partida:")
            .setView(input)
            .setPositiveButton("Guardar") { dialog, _ ->
                val nombre = input.text.toString().trim()
                if (nombre.isNotEmpty()) {
                    juego.nombrePartida = nombre
                    juego.fecha = System.currentTimeMillis().toString()

                    magicService.guardarPartida(juego,
                        onSuccess = {
                            runOnUiThread {
                                Toast.makeText(this, "Partida guardada con éxito", Toast.LENGTH_SHORT).show()
                            }
                            dialog.dismiss()
                        },
                        onFailure = {
                            runOnUiThread {
                                Toast.makeText(this, "Error al guardar la partida", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                } else {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
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
        for ((jugador, _) in juego.getJugadoresConVidas()) {
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
        return juego.getJugadoresConVidas().maxByOrNull { juego.getVida(it.key) ?: 0 }?.key ?: -1
    }
}
