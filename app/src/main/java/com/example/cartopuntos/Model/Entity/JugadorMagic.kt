package com.example.cartopuntos.Model.Entity

data class JugadorMagic(
    val id: Int,                // ID del jugador.
    val nombre: String,         // Nombre del jugador.
    var vida: Int,              // Vida actual.
    val vidaInicial: Int = 40,  // Vida inicial (puede cambiarse si se juega con diferentes reglas).

    // Contadores adicionales
    var contadorVeneno: Int = 0,
    var contadorTesoros: Int = 0,
    var contadorEnergia: Int = 0,
    var contadorMasUno: Int = 0, // +1/+1
    var esMonarca: Boolean = false,
    var tieneIniciativa: Boolean = false,

    // Comandante (daño recibido de otros jugadores)
    val dañoDeComandantes: MutableMap<Int, Int> = mutableMapOf()// clave: ID del comandante enemigo
)
