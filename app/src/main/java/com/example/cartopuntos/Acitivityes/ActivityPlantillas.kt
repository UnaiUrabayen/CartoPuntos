package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
        Log.d("ActivityPlantillas", "Entrando a onCreate")
        setContentView(R.layout.view_plantillas)

        // Configurar RecyclerView
        recyclerViewPlantillas = findViewById(R.id.recyclerViewPlantillas)
        recyclerViewPlantillas.layoutManager = LinearLayoutManager(this)

        // Asignar el adapter
        adapter = PlantillasAdapter(this, plantillasList)
        recyclerViewPlantillas.adapter = adapter

        // Obtener datos de Firebase
        obtenerPlantillasDesdeFirebase()

        val btnCrearPlantilla = findViewById<Button>(R.id.btnCrearPlantilla)
        btnCrearPlantilla.setOnClickListener {
            val intent = Intent(this, CrearPlantillaActivity::class.java)
            startActivity(intent)
        }

    }

    private fun obtenerPlantillasDesdeFirebase() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        Log.d("ActivityPlantillas", "Usuario actual: $currentUser")
        if (currentUser != null) {
            Log.d("ActivityPlantillas", "Obteniendo plantillas para el usuario: ${currentUser.uid}")

            db.collection("usuarios")
                .document(currentUser.uid)
                .collection("plantillas")
                .get()
                .addOnSuccessListener { result ->
                    Log.d("ActivityPlantillas", "Plantillas obtenidas: ${result.size()}") // Verifica la cantidad de documentos obtenidos

                    plantillasList.clear()  // Limpia la lista antes de cargar nuevos datos

                    if (result.isEmpty) {
                        // Si no se encuentran datos, mostramos un mensaje de error
                        Toast.makeText(this, "No se encontraron plantillas", Toast.LENGTH_SHORT).show()
                    } else {
                        // Cargar las plantillas en la lista
                        for (document in result) {
                            val plantilla = document.toObject(PlantillaPerfil::class.java)
                            plantillasList.add(plantilla)
                            Log.d("ActivityPlantillas", "Plantilla añadida: ${plantilla.nombreJugador}")
                        }
                    }

                    // Solo actualizamos el adapter si hay datos
                    if (plantillasList.isNotEmpty()) {
                        // Notificar al adapter que los datos han cambiado
                        adapter.notifyDataSetChanged()
                    } else {
                        // Si la lista está vacía, mostramos un mensaje indicando que no hay plantillas
                        Toast.makeText(this, "No hay plantillas para mostrar", Toast.LENGTH_SHORT).show()
                    }

                    // Log de verificación para asegurarse de que los datos se cargaron correctamente
                    Log.d("ActivityPlantillas", "Número de plantillas en la lista: ${plantillasList.size}")
                }
                .addOnFailureListener { exception ->
                    // Log de error en caso de que falle la consulta
                    Log.e("ActivityPlantillas", "Error obteniendo plantillas", exception)
                    Toast.makeText(this, "Error al cargar plantillas", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Si el usuario no está autenticado
            Log.e("ActivityPlantillas", "Usuario no autenticado")
            Toast.makeText(this, "Debe iniciar sesión para ver las plantillas", Toast.LENGTH_LONG).show()
        }
    }
}
