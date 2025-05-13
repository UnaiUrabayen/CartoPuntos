package com.example.cartopuntos.activities // Asegúrate de que el nombre del paquete esté correcto.

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartopuntos.Acitivityes.CrearPlantillaActivity
import com.example.cartopuntos.Model.Entity.PlantillaPerfil
import com.example.cartopuntos.R
import com.example.cartopuntos.model.adapter.PlantillasAdapter


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityPlantillas : AppCompatActivity() {

    private lateinit var recyclerViewPlantillas: RecyclerView
    private lateinit var adapter: PlantillasAdapter
    private val plantillasList = mutableListOf<PlantillaPerfil>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_plantillas)

        // Verificar si venimos del juego
        val origen = intent.getStringExtra("ORIGEN")
        val isDesdeJuego = origen == "juego"  // Chequea si la fuente es "juego"

        // Configurar RecyclerView
        recyclerViewPlantillas = findViewById(R.id.recyclerViewPlantillas)
        recyclerViewPlantillas.layoutManager = LinearLayoutManager(this)

        // Pasar el valor de isDesdeJuego al adaptador
        adapter = PlantillasAdapter(
            context = this,
            plantillas = plantillasList,
            isDesdeJuego = isDesdeJuego  // Pasamos el valor de isDesdeJuego
        )
        recyclerViewPlantillas.adapter = adapter

        // Obtener datos de Firebase
        obtenerPlantillasDesdeFirebase()

        // Configurar botón para crear plantillas
        val btnCrearPlantilla = findViewById<Button>(R.id.btnCrearPlantilla)
        btnCrearPlantilla.setOnClickListener {
            val intent = Intent(this, CrearPlantillaActivity::class.java)
            startActivityForResult(intent, 123)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            // Si regresamos de CrearPlantillaActivity y el resultado es OK,
            // volvemos a cargar las plantillas desde Firebase
            obtenerPlantillasDesdeFirebase()
        }
    }

    // Función para obtener las plantillas desde Firebase
    private fun obtenerPlantillasDesdeFirebase() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Verificar que el usuario esté autenticado
        if (currentUser != null) {
            db.collection("usuarios")
                .document(currentUser.uid)
                .collection("plantillas")
                .get()
                .addOnSuccessListener { result ->
                    plantillasList.clear()  // Limpiar lista antes de cargar
                    for (document in result) {
                        val plantilla = document.toObject(PlantillaPerfil::class.java)
                        plantillasList.add(plantilla)
                    }
                    adapter.notifyDataSetChanged()  // Notificar al adapter para que se actualice el RecyclerView
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al cargar plantillas", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Debe iniciar sesión para ver las plantillas", Toast.LENGTH_SHORT).show()
        }
    }

    fun devolverPlantillaSeleccionada(plantilla: PlantillaPerfil) {
        val intent = Intent()
        intent.putExtra("PLANTILLA", plantilla)
        setResult(RESULT_OK, intent)
        finish()
    }

}
