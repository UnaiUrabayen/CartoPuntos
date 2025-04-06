package com.example.cartopuntos.Model.Entity

data class User(
    val name: String = "",
    val email: String = "",
    val passwordHash: String = "" // Usamos el hash de la contraseña
)