package com.example.cartopuntos.Model.Entity

data class JugadorMagic(
    val id: Int,               // Añadimos un ID para poder identificar a los jugadores.
    val nombre: String,        // Nombre del jugador.
    var vida: Int,             // Vida actual del jugador.
    val vidaInicial: Int = 40  // Vida inicial, esta puede ser cambiada desde la lógica del juego.
)
