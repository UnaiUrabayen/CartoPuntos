package com.example.cartopuntos.Model.Entity

import java.util.UUID

data class PlantillaPerfil(
    val id: String = UUID.randomUUID().toString(),  // Generar un ID único
    val nombreJugador: String,
    val fotoPerfilUrl: String,
    val fondoUrl: String
)
