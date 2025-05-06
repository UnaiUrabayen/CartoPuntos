package com.example.cartopuntos.Logica

class JuegoMagic(
    val cantidadJugadores: Int,
    val vidaInicial: Int = 40,
    val permitirVidaNegativa: Boolean = false,
    val autoReinicioActivo: Boolean = false,
    val onJugadorMuere: ((Int) -> Unit)? = null,
    val onGanadorDetectado: ((Int) -> Unit)? = null
) {
    private val vidas = mutableMapOf<Int, Int>()

    init {
        for (jugador in 1..cantidadJugadores) {
            vidas[jugador] = vidaInicial
        }
    }

    fun getVida(jugador: Int): Int? = vidas[jugador]

    fun modificarVida(jugador: Int, delta: Int): Boolean {
        val vidaActual = vidas[jugador] ?: return false
        val nuevaVida = vidaActual + delta

        if (nuevaVida < 0 && !permitirVidaNegativa) return false

        vidas[jugador] = nuevaVida

        if (vidaActual > 0 && nuevaVida <= 0) {
            onJugadorMuere?.invoke(jugador)
        }

        if (autoReinicioActivo && vidas.count { it.value > 0 } == 1) {
            val ganador = vidas.entries.first { it.value > 0 }.key
            onGanadorDetectado?.invoke(ganador)
        }

        return true
    }

    fun reiniciar() {
        for (jugador in vidas.keys) {
            vidas[jugador] = vidaInicial
        }
    }
    fun lanzarDado(caras: Int): Int {
        require(caras > 0) { "El dado debe tener al menos 1 cara." }
        return (1..caras).random()
    }

    fun getJugadores(): Map<Int, Int> = vidas.toMap()
}
