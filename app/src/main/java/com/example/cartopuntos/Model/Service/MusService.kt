package com.example.cartopuntos.Model.Service

import android.util.Log
import com.example.cartopuntos.Model.Entity.PartidaMus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MusService {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Guardar partida
    fun guardarPartida(partida: PartidaMus, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure(Exception("Usuario no logueado"))
            return
        }

        val partidasRef = db.collection("partidasMus")

        partidasRef.document(partida.id)
            .set(partida)
            .addOnSuccessListener {
                Log.d("MusService", "Partida guardada con éxito")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("MusService", "Error al guardar partida", it)
                onFailure(it)
            }
    }

    // Eliminar partida
    fun eliminarPartida(partida: PartidaMus, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure(Exception("Usuario no logueado"))
            return
        }

        val partidasRef = db.collection("partidasMus")

        partidasRef.document(partida.id)
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
    // Obtener partidas del usuario actual
    fun obtenerPartidasDelUsuario(onSuccess: (List<PartidaMus>) -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure(Exception("Usuario no logueado"))
            return
        }

        val partidasRef = db.collection("partidasMus")
            .whereEqualTo("usuarioId", currentUser.uid)  // Filtro para el usuario actual

        partidasRef.get()
            .addOnSuccessListener { querySnapshot ->
                val partidas = mutableListOf<PartidaMus>()
                for (document in querySnapshot.documents) {
                    val partida = document.toObject(PartidaMus::class.java)
                    if (partida != null) {
                        partidas.add(partida)
                    }
                }
                onSuccess(partidas)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


}
