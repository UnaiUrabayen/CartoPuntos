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

                    // Verificación de los campos de la partida y el ID
                    if (partida != null) {
                        Log.d("MusService", "Partida obtenida: ${partida.nombrePartida}, ID: ${partida.id}")
                        if (partida.id.isNotEmpty()) {
                            partidas.add(partida)
                        } else {
                            Log.e("MusService", "Partida con ID vacío encontrada, no se añadirá.")
                        }
                    } else {
                        Log.e("MusService", "No se pudo convertir el documento a PartidaMus. Documento: $document")
                    }
                }
                if (partidas.isEmpty()) {
                    Log.d("MusService", "No se encontraron partidas para el usuario.")
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
        FirebaseFirestore.getInstance().collection("partidasMus")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val partida = document.toObject(PartidaMus::class.java)
                    if (partida != null) {
                        partida.id = document.id
                        onSuccess(partida)
                    } else {
                        onFailure()
                    }
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }




}
