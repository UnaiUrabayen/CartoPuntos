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
        auth.createUserWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Si el usuario se ha creado correctamente, se guarda en Firestore
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
                    // Si falla, mostramos el error de Firebase
                    val errorMessage = task.exception?.localizedMessage
                    onComplete(false, errorMessage ?: "Ha ocurrido un error al crear la cuenta.")
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
                    // Si la sesión es exitosa, obtenemos los datos del usuario
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
                    // Si falla el inicio de sesión, mostramos el error de Firebase
                    val errorMessage = task.exception?.localizedMessage
                    onComplete(false, errorMessage ?: "Error al iniciar sesión. Verifica tu email y contraseña.", null)
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
}
