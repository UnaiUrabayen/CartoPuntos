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
        // Validación de la contraseña
        val errorMessage = validarContrasena(contrasena)
        if (errorMessage != null) {
            onComplete(false, errorMessage) // Devuelve el mensaje de error de contraseña
            return
        }

        // Crear usuario en Firebase Authentication con email y contraseña
        auth.createUserWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Crear un objeto User con nombreUsuario y email
                    val usuario = User(nombreUsuario, email)

                    // Guardar el usuario en Firestore, usando el email como ID del documento
                    db.collection("usuarios")
                        .document(email)  // Usar el email como ID del documento
                        .set(usuario)     // Guardar el nombre de usuario y email
                        .addOnSuccessListener {
                            onComplete(true, null) // Usuario creado con éxito en Firestore
                        }
                        .addOnFailureListener { e ->
                            onComplete(false, e.message) // Error al guardar en Firestore
                        }
                } else {
                    onComplete(false, task.exception?.message) // Error al crear usuario en Firebase Authentication
                }
            }
    }

    // Función para validar la contraseña
    private fun validarContrasena(contrasena: String): String? {
        // Verificar que la contraseña tenga al menos 6 caracteres
        if (contrasena.length < 6) {
            return "La contraseña debe tener al menos 6 caracteres."
        }

        // Verificar que la contraseña contenga al menos una letra mayúscula, una minúscula y un número
        val hasUpperCase = contrasena.any { it.isUpperCase() }
        val hasLowerCase = contrasena.any { it.isLowerCase() }
        val hasDigit = contrasena.any { it.isDigit() }

        if (!hasUpperCase || !hasLowerCase || !hasDigit) {
            return "La contraseña debe contener al menos una letra mayúscula, una minúscula y un número."
        }

        return null // Si la contraseña es válida, retornar null (sin errores)
    }

    // Función para iniciar sesión con email y contraseña
    fun iniciarSesion(email: String, contrasena: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Inicio de sesión exitoso
                } else {
                    onComplete(false, task.exception?.message) // Error al iniciar sesión
                }
            }
    }
}
