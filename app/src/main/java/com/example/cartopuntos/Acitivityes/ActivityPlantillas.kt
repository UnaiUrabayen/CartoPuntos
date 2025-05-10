package com.example.cartopuntos.Acitivityes

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartopuntos.Model.Entity.PlantillaPerfil
import com.example.cartopuntos.Model.Entity.PlantillasAdapter
import com.example.cartopuntos.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityPlantillas : AppCompatActivity() {

    private lateinit var recyclerViewPlantillas: RecyclerView
    private lateinit var adapter: PlantillasAdapter
    private val plantillasList = mutableListOf<PlantillaPerfil>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_plantillas)

        // Configurar RecyclerView
        recyclerViewPlantillas = findViewById(R.id.recyclerViewPlantillas)
        recyclerViewPlantillas.layoutManager = LinearLayoutManager(this)

        // Asignar el adapter primero
        adapter = PlantillasAdapter(this, plantillasList)
        recyclerViewPlantillas.adapter = adapter

        // Obtener datos de Firebase
        obtenerPlantillasDesdeFirebase()
    }


    private fun obtenerPlantillasDesdeFirebase() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            Log.d("ActivityPlantillas", "Obteniendo plantillas para el usuario: ${currentUser.uid}")

            db.collection("usuarios")
                .document(currentUser.uid)
                .collection("plantillas")
                .get()
                .addOnSuccessListener { result ->
                    Log.d(
                        "ActivityPlantillas",
                        "Plantillas obtenidas: ${result.size()}"
                    ) // Verifica la cantidad de documentos obtenidos
                    plantillasList.clear()  // Limpia la lista antes de cargar
                    if (result.isEmpty) {
                        Toast.makeText(this, "No hay plantillas para mostrar", Toast.LENGTH_SHORT)
                            .show()
                    }
                    for (document in result) {
                        val plantilla = document.toObject(PlantillaPerfil::class.java)
                        plantillasList.add(plantilla)
                        Log.d("ActivityPlantillas", "Plantilla añadida: ${plantilla.nombreJugador}")
                    }
                    Log.d(
                        "ActivityPlantillas",
                        "Número de plantillas en la lista: ${plantillasList.size}"
                    )
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.e("ActivityPlantillas", "Error obteniendo plantillas", exception)
                    Toast.makeText(this, "Error al cargar plantillas", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.e("ActivityPlantillas", "Usuario no autenticado")
        }
    }
}
