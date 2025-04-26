package com.example.cartopuntos.Model.Service

import com.example.cartopuntos.Model.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsuarioService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Función para crear un nuevo usuario
    fun crearUsuario(
        nombreUsuario: String,
        email: String,
        contrasena: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val errorMessage = validarContrasena(contrasena)
        if (errorMessage != null) {
            onComplete(false, errorMessage)
            return
        }

        auth.createUserWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val usuario = User(nombreUsuario, email)
                    db.collection("usuarios")
                        .document(email)
                        .set(usuario)
                        .addOnSuccessListener {
                            onComplete(true, null)
                        }
                        .addOnFailureListener {
                            onComplete(false, "Ha ocurrido un error al guardar el usuario.")
                        }
                } else {
                    onComplete(false, "Ha ocurrido un error al crear la cuenta.")
                }
            }
    }

    // Función para iniciar sesión
    fun iniciarSesion(
        email: String,
        contrasena: String,
        onComplete: (Boolean, String?, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("usuarios")
                        .document(email)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val nombreUsuario = document.getString("nombreUsuario")
                                onComplete(true, null, nombreUsuario)
                            } else {
                                onComplete(false, "Usuario no encontrado.", null)
                            }
                        }
                        .addOnFailureListener {
                            onComplete(false, "Ha ocurrido un error al obtener los datos.", null)
                        }
                } else {
                    onComplete(false, "Error al iniciar sesión. Verifica tu email y contraseña.", null)
                }
            }
    }

    // NUEVO: Función para obtener el usuario actualmente logueado
    fun obtenerUsuarioActual(onComplete: (User?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                db.collection("usuarios")
                    .document(email)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val nombreUsuario = document.getString("nombreUsuario") ?: "Usuario"
                            val user = User(nombreUsuario, email)
                            onComplete(user)
                        } else {
                            onComplete(null)
                        }
                    }
                    .addOnFailureListener {
                        onComplete(null)
                    }
            } else {
                onComplete(null)
            }
        } else {
            onComplete(null)
        }
    }

    // Validar contraseña
    private fun validarContrasena(contrasena: String): String? {
        if (contrasena.length < 6) return "La contraseña debe tener al menos 6 caracteres."
        val hasUpperCase = contrasena.any { it.isUpperCase() }
        val hasLowerCase = contrasena.any { it.isLowerCase() }
        val hasDigit = contrasena.any { it.isDigit() }
        if (!hasUpperCase || !hasLowerCase || !hasDigit) {
            return "La contraseña debe contener al menos una letra mayúscula, una minúscula y un número."
        }
        return null
    }
}
