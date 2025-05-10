package com.example.cartopuntos.Model.Service

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartopuntos.Model.Entity.PlantillaPerfil
import com.example.cartopuntos.R
import com.google.firebase.firestore.FirebaseFirestore

class ObtnerPlantilla : AppCompatActivity() {
/*
    private lateinit var recyclerView: RecyclerView
    private lateinit var plantillasAdapter: PlantillasAdapter
    private lateinit var plantillasList: MutableList<PlantillaPerfil>
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_plantillas)

        db = FirebaseFirestore.getInstance()

        val jugadorId = intent.getStringExtra("jugadorId")

        if (jugadorId == null) {
            Log.e("ObtnerPlantilla", "Jugador ID no encontrado")
            Toast.makeText(this, "Jugador no válido", Toast.LENGTH_SHORT).show()
            return
        }

        plantillasList = mutableListOf()
        recyclerView = findViewById(R.id.recyclerViewPlantillas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ Adaptador corregido con todos los parámetros requeridos
        plantillasAdapter = PlantillasAdapter(
            context = this,
            plantillas = plantillasList,
            isSelectable = false, // o true si quieres que se seleccione una
            onPlantillaSelected = { plantillaSeleccionada ->
                Toast.makeText(this, "Seleccionada: ${plantillaSeleccionada.nombreJugador}", Toast.LENGTH_SHORT).show()
                // Podrías devolver resultado con setResult(...) si lo necesitas
            }
        )

        recyclerView.adapter = plantillasAdapter
        loadPlantillasForJugador(jugadorId)
    }

    private fun loadPlantillasForJugador(jugadorId: String) {
        val usuarioId = getUsuarioIdFromJugadorId(jugadorId)
        if (usuarioId != null) {
            db.collection("usuarios")
                .document(usuarioId)
                .collection("plantillas")
                .get()
                .addOnSuccessListener { documents ->
                    plantillasList.clear()
                    for (document in documents) {
                        val plantilla = document.toObject(PlantillaPerfil::class.java)
                        plantillasList.add(plantilla)
                    }
                    plantillasAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.w("ObtnerPlantilla", "Error obteniendo plantillas", exception)
                    Toast.makeText(this, "Error al obtener plantillas", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.e("ObtnerPlantilla", "UsuarioId no encontrado para jugadorId: $jugadorId")
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUsuarioIdFromJugadorId(jugadorId: String): String? {
        return when (jugadorId) {
            "jugador1" -> "usuarioId1"
            "jugador2" -> "usuarioId2"
            "jugador3" -> "usuarioId3"
            "jugador4" -> "usuarioId4"
            else -> null
        }
    }
 */
}
