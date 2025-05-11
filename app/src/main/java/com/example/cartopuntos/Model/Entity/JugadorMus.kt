package com.example.cartopuntos.Model.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "jugadores_mus")
data class JugadorMus(
    @PrimaryKey val idJugador: String = "", // ID único del jugador
    var nombreJugador: String = "",        // Nombre del jugador
    var uidUsuario: String = "",          // ID del usuario relacionado con este jugador
    var plantilla: PlantillaPerfil? = null // Plantilla asociada al jugador (ahora solo 1 plantilla)
) : Serializable {

    // Constructor sin parámetros (obligatorio para Firebase)
    constructor() : this(
        idJugador = "",
        nombreJugador = "",
        uidUsuario = "",
        plantilla = null
    )
}
