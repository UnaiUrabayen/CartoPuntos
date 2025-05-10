package com.example.cartopuntos.Model.Entity

import java.util.UUID

data class PlantillaPerfil(
    var idPlantilla: String = "",
    var uidUsuario: String = "",
    var nombreJugador: String = "",
    var fotoPerfilUrl: String = "",
    var urlFondo: String = ""
)
