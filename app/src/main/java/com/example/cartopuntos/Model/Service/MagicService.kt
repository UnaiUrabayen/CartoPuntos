package com.example.cartopuntos.Model.Service

import com.example.cartopuntos.Logica.JuegoMagic
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MagicService {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun guardarPartida(partida: JuegoMagic, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure(Exception("Usuario no logueado"))
            return
        }

        // Convertir los jugadores a un mapa con claves de tipo String
        val jugadoresConVidasString = partida.convertirJugadoresAStringMap()

        // Crear un mapa para guardar los datos en Firestore
        val datosPartida = mapOf(
            "jugadoresConVidas" to jugadoresConVidasString,
            "nombrePartida" to partida.nombrePartida,
            "fecha" to partida.fecha
        )

        db.collection("usuarios")
            .document(currentUser.uid)
            .collection("partidasMagic")
            .document(partida.id)
            .set(datosPartida)
            .addOnSuccessListener {
                Log.d("MagicService", "Partida de Magic guardada con éxito")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("MagicService", "Error al guardar partida", it)
                onFailure(it)
            }
    }

    fun eliminarPartida(partida: JuegoMagic, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure(Exception("Usuario no logueado"))
            return
        }

        db.collection("usuarios")
            .document(currentUser.uid)
            .collection("partidasMagic")
            .document(partida.id)
            .delete()
            .addOnSuccessListener {
                Log.d("MagicService", "Partida de Magic eliminada con éxito")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("MagicService", "Error al eliminar partida", it)
                onFailure(it)
            }
    }

    fun obtenerPartidasDelUsuario(onSuccess: (List<JuegoMagic>) -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure(Exception("Usuario no logueado"))
            return
        }

        db.collection("usuarios")
            .document(currentUser.uid)
            .collection("partidasMagic")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val partidas = mutableListOf<JuegoMagic>()
                for (document in querySnapshot.documents) {
                    val partida = document.toObject(JuegoMagic::class.java)
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
        onSuccess: (JuegoMagic) -> Unit,
        onFailure: () -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure()
            return
        }

        db.collection("usuarios")
            .document(currentUser.uid)
            .collection("partidasMagic")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                val partida = document.toObject(JuegoMagic::class.java)
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
