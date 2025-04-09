package com.example.cartopuntos.Model.Service

import com.example.cartopuntos.Model.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsuarioService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun crearUsuario(
        nombreUsuario: String,
        email: String,
        contrasena: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    val uid = user?.uid ?: return@addOnCompleteListener
                    val usuario = User(uid, nombreUsuario, email)

                    db.collection("usuarios")
                        .add(usuario)
                        .addOnSuccessListener {
                            onComplete(true, null)
                        }
                        .addOnFailureListener { e ->
                            onComplete(false, e.message)
                        }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
        }
    }
