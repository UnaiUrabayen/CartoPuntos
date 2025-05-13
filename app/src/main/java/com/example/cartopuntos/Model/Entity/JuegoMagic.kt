package com.example.cartopuntos.Logica

import JugadorMagic
import java.text.SimpleDateFormat
import java.util.*

class JuegoMagic(
    var id: String = UUID.randomUUID().toString(),
    var cantidadJugadores: Int = 4,  // Asegúrate de que este parámetro está correctamente declarado
    var vidaInicial: Int = 40,       // Asegúrate de que este parámetro también está declarado
    var permitirVidaNegativa: Boolean = false,
    var autoReinicioActivo: Boolean = false,
    var onJugadorMuere: ((Int) -> Unit)? = null,
    var onGanadorDetectado: ((Int) -> Unit)? = null,
    var nombrePartida: String = "",
    var fecha: String = getFechaActual()
) {
    // Aquí almacenamos los jugadores y sus datos
    val jugadores = mutableMapOf<Int, JugadorMagic>()

    init {
        // Crear los jugadores
        for (i in 1..cantidadJugadores) {
            jugadores[i] = JugadorMagic(id = i.toString(), nombre = "Jugador $i", vida = vidaInicial)
        }
    }

    fun getVida(jugadorId: Int): Int? = jugadores[jugadorId]?.vida

    fun modificarVida(jugadorId: Int, delta: Int): Boolean {
        val jugador = jugadores[jugadorId] ?: return false
        val vidaActual = jugador.vida
        val nuevaVida = vidaActual + delta
        if (nuevaVida < 0 && !permitirVidaNegativa) return false

        jugador.vida = nuevaVida

        if (vidaActual > 0 && nuevaVida <= 0) {
            onJugadorMuere?.invoke(jugadorId)
        }

        if (autoReinicioActivo && jugadores.count { it.value.vida > 0 } == 1) {
            val ganador = jugadores.entries.first { it.value.vida > 0 }.key
            onGanadorDetectado?.invoke(ganador)
        }

        return true
    }

    fun convertirJugadoresAStringMap(): Map<String, JugadorMagic> {
        return jugadores.mapKeys { it.key.toString() }
    }

    fun reiniciar() {
        for (jugador in jugadores.keys) {
            jugadores[jugador]?.vida = vidaInicial
        }
    }

    fun lanzarDado(caras: Int): Int {
        require(caras > 0) { "El dado debe tener al menos 1 cara." }
        return (1..caras).random()
    }

    fun getJugadoresConVidas(): Map<Int, Int> = jugadores.mapValues { it.value.vida }

    companion object {
        private fun getFechaActual(): String {
            val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return formato.format(Date())
        }
    }
}
