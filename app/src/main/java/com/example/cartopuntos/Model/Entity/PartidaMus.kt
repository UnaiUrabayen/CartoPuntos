package com.example.cartopuntos.Model.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidas")
data class PartidaMus(
    @PrimaryKey val id: String,
    var fecha: Long,
    var puntosJugador1: Int,
    var puntosJugador2: Int,
    var puntosJugador3: Int,
    var puntosJugador4: Int,
    var puntosEquipo1: Int,
    var puntosEquipo2: Int,
    var victoriasEquipo1: Int,
    var victoriasEquipo2: Int,
    var equipoGanador: String?,
    var nombrePartida: String,
    var cantidadRondas: Int, // Nueva propiedad para almacenar la cantidad de rondas necesarias para ganar
    var jugadores: List<JugadorMus> = emptyList() // Lista de jugadores asociados a la partida
)
