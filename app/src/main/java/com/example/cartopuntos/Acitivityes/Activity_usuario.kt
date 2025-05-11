package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartopuntos.Model.Adapter.PartidaAdapter
import com.example.cartopuntos.Model.Entity.PartidaMus
import com.example.cartopuntos.Model.Service.MusService
import com.example.cartopuntos.R
import com.example.cartopuntos.activities.ActivityPlantillas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Activity_usuario : AppCompatActivity() {

    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvEmailUsuario: TextView
    private lateinit var tvTotalPlantillas: TextView
    private lateinit var btnPlantillas: Button
    private lateinit var btnLogout: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var partidaAdapter: PartidaAdapter
    private val listaPartidas = mutableListOf<PartidaMus>()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val musService = MusService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ActivityUsuario", "onCreate llamado")

        setContentView(R.layout.view_activity_usuario) // ¡Importante! Antes de acceder a las vistas

        // Enlazar vistas
        tvNombreUsuario = findViewById(R.id.tv_nombreUsuario)
        tvEmailUsuario = findViewById(R.id.tv_emailUsuario)
        tvTotalPlantillas = findViewById(R.id.tv_totalPartidas)
        btnPlantillas = findViewById(R.id.btn_plantillas)
        btnLogout = findViewById(R.id.imb_logout)
        btnBack = findViewById(R.id.imb_atras)
        recyclerView = findViewById(R.id.rv_partidas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        partidaAdapter = PartidaAdapter(listaPartidas) { partidaEliminada ->
            listaPartidas.remove(partidaEliminada)
            partidaAdapter.notifyDataSetChanged()
            tvTotalPlantillas.text = "Total de partidas: ${listaPartidas.size}"
        }
        recyclerView.adapter = partidaAdapter



        val user = auth.currentUser
        if (user != null) {
            val emailUsuario = user.email ?: "user@thx.com"

            // Mostrar datos del usuario
            db.collection("usuarios").document(emailUsuario)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nombreUsuario = document.getString("nombreUsuario") ?: "Usuario"
                        tvNombreUsuario.text = "Hola, $nombreUsuario!"
                        tvEmailUsuario.text = "Email: $emailUsuario"
                    }
                }
                .addOnFailureListener {
                    tvNombreUsuario.text = "Error al obtener datos del usuario"
                }

            // Cargar partidas del usuario
            musService.obtenerPartidasDelUsuario(
                onSuccess = { partidas ->
                    listaPartidas.clear()
                    listaPartidas.addAll(partidas)
                    partidaAdapter.notifyDataSetChanged()
                    tvTotalPlantillas.text = "Total de partidas: ${listaPartidas.size}"
                },
                onFailure = {
                    Toast.makeText(this, "Error al cargar partidas", Toast.LENGTH_SHORT).show()
                }
            )

        } else {
            startActivity(
                Intent(this, IniciarSesionActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            finish()
        }

        // Botones
        btnPlantillas.setOnClickListener {
            startActivity(Intent(this, ActivityPlantillas::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(
                Intent(this, IniciarSesionActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
