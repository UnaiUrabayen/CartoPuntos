package com.example.cartopuntos.Model.Entity

data class User(
    val nombreUsuario: String = "",
    val email: String = "",
    val plantillas: List<PlantillaPerfil> = emptyList()
)
