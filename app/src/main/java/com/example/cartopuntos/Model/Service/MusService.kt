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

        val partidasRef = db.collection("usuarios")
            .document(currentUser.uid)
            .collection("partidasMus")

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
}
