package com.example.cartopuntos.Logica

import com.example.cartopuntos.Model.Entity.JugadorMagic

class JuegoMagic(
    val cantidadJugadores: Int,
    val vidaInicial: Int = 40,
    val permitirVidaNegativa: Boolean = false,
    val autoReinicioActivo: Boolean = false,
    val onJugadorMuere: ((Int) -> Unit)? = null,
    val onGanadorDetectado: ((Int) -> Unit)? = null
) {
    // Aquí almacenamos los jugadores y sus datos
    val jugadores = mutableMapOf<Int, JugadorMagic>()

    init {
        // Crear los jugadores
        for (i in 1..cantidadJugadores) {
            jugadores[i] = JugadorMagic(id = i, nombre = "Jugador $i", vida = vidaInicial)
        }
    }

    // Obtener la vida de un jugador
    fun getVida(jugadorId: Int): Int? = jugadores[jugadorId]?.vida

    // Modificar la vida de un jugador (agregar o quitar vida)
    fun modificarVida(jugadorId: Int, delta: Int): Boolean {
        val jugador = jugadores[jugadorId] ?: return false
        val vidaActual = jugador.vida
        val nuevaVida = vidaActual + delta

        // No permitir vida negativa si está desactivado
        if (nuevaVida < 0 && !permitirVidaNegativa) return false

        jugador.vida = nuevaVida

        // Si la vida del jugador llega a 0, se invoca la muerte del jugador
        if (vidaActual > 0 && nuevaVida <= 0) {
            onJugadorMuere?.invoke(jugadorId)
        }

        // Si está activado el reinicio automático, detectamos al ganador cuando solo queda un jugador vivo
        if (autoReinicioActivo && jugadores.count { it.value.vida > 0 } == 1) {
            val ganador = jugadores.entries.first { it.value.vida > 0 }.key
            onGanadorDetectado?.invoke(ganador)
        }

        return true
    }

    // Función para aplicar daño por comandante a un jugador específico
    fun infligirDañoPorComandante(jugadorId: Int, comandanteId: Int, daño: Int) {
        val jugador = jugadores[jugadorId] ?: return
        // Si no hay un registro de daño para ese comandante, lo inicializamos
        if (!jugador.dañoDeComandantes.containsKey(comandanteId)) {
            jugador.dañoDeComandantes[comandanteId] = 0
        }
        // Sumamos el daño recibido de este comandante
        jugador.dañoDeComandantes[comandanteId] = jugador.dañoDeComandantes[comandanteId]!! + daño
    }

    // Función para obtener el daño total de un jugador por un comandante específico
    fun obtenerDañoDeComandante(jugadorId: Int, comandanteId: Int): Int {
        val jugador = jugadores[jugadorId] ?: return 0
        return jugador.dañoDeComandantes[comandanteId] ?: 0
    }

    // Reiniciar el juego: restablece la vida de todos los jugadores
    fun reiniciar() {
        for (jugador in jugadores.keys) {
            jugadores[jugador]?.vida = vidaInicial
            // Reiniciar los daños de los comandantes al reiniciar el juego
            jugadores[jugador]?.dañoDeComandantes?.clear()
        }
    }

    // Función para lanzar un dado con un número de caras determinado
    fun lanzarDado(caras: Int): Int {
        require(caras > 0) { "El dado debe tener al menos 1 cara." }
        return (1..caras).random()
    }

    // Obtener los jugadores con sus vidas actuales (modificado para no generar conflicto)
    fun getJugadoresConVidas(): Map<Int, Int> = jugadores.mapValues { it.value.vida }

}
