package com.example.cartopuntos.Model.Entity

import java.util.UUID

import java.io.Serializable

data class PlantillaPerfil(
    val idPlantilla: String = "",
    val uidUsuario: String = "",
    val nombreJugador: String = "",
    val urlFondo: String = "",
    val fotoPerfilUrl: String = ""
) : Serializable
