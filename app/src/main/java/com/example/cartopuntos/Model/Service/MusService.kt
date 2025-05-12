package com.example.cartopuntos.Model.Service

import android.util.Log
import com.example.cartopuntos.Model.Entity.PartidaMus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MusService {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun guardarPartida(partida: PartidaMus, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure(Exception("Usuario no logueado"))
            return
        }

        db.collection("usuarios")
            .document(currentUser.uid)
            .collection("partidasMus")
            .document(partida.id)
            .set(partida)
            .addOnSuccessListener {
                Log.d("MusService", "Partida guardada con éxito en subcolección")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("MusService", "Error al guardar partida", it)
                onFailure(it)
            }
    }

    fun eliminarPartida(partida: PartidaMus, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure(Exception("Usuario no logueado"))
            return
        }

        db.collection("usuarios")
            .document(currentUser.uid)
            .collection("partidasMus")
            .document(partida.id)
            .delete()
            .addOnSuccessListener {
                Log.d("MusService", "Partida eliminada con éxito")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("MusService", "Error al eliminar partida", it)
                onFailure(it)
            }
    }

    fun obtenerPartidasDelUsuario(onSuccess: (List<PartidaMus>) -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure(Exception("Usuario no logueado"))
            return
        }

        db.collection("usuarios")
            .document(currentUser.uid)
            .collection("partidasMus")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val partidas = mutableListOf<PartidaMus>()
                for (document in querySnapshot.documents) {
                    val partida = document.toObject(PartidaMus::class.java)
                    if (partida != null && partida.id.isNotEmpty()) {
                        partidas.add(partida)
                    }
                }
                onSuccess(partidas)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun obtenerPartidaPorId(
        id: String,
        onSuccess: (PartidaMus) -> Unit,
        onFailure: () -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure()
            return
        }

        db.collection("usuarios")
            .document(currentUser.uid)
            .collection("partidasMus")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                val partida = document.toObject(PartidaMus::class.java)
                if (partida != null) {
                    partida.id = document.id
                    onSuccess(partida)
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }
}
