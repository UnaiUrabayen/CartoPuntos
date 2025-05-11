package com.example.cartopuntos.Model.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "partidas")
data class PartidaMus(
    @PrimaryKey val id: String = "",
    var fecha: Long = 0L,
    var puntosJugador1: Int = 0,
    var puntosJugador2: Int = 0,
    var puntosJugador3: Int = 0,
    var puntosJugador4: Int = 0,
    var puntosEquipo1: Int = 0,
    var puntosEquipo2: Int = 0,
    var victoriasEquipo1: Int = 0,
    var victoriasEquipo2: Int = 0,
    var equipoGanador: String? = null,
    var nombrePartida: String = "",
    var cantidadRondas: Int = 0,
    var jugadores: List<JugadorMus> = emptyList(),
    var usuarioId: String = ""
) : Serializable {
    // Constructor sin parámetros (obligatorio para Firebase y serialización)
    constructor() : this(
        id = "",
        fecha = 0L,
        puntosJugador1 = 0,
        puntosJugador2 = 0,
        puntosJugador3 = 0,
        puntosJugador4 = 0,
        puntosEquipo1 = 0,
        puntosEquipo2 = 0,
        victoriasEquipo1 = 0,
        victoriasEquipo2 = 0,
        equipoGanador = null,
        nombrePartida = "",
        cantidadRondas = 0,
        jugadores = emptyList(),
        usuarioId = ""
    )
}
