package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartopuntos.Logica.JuegoMagic
import com.example.cartopuntos.Model.Adapter.PartidaAdapter
import com.example.cartopuntos.Model.Adapter.PartidaMagicAdapter
import com.example.cartopuntos.Model.Entity.PartidaMus
import com.example.cartopuntos.Model.Service.MagicService
import com.example.cartopuntos.Model.Service.MusService
import com.example.cartopuntos.R
import com.example.cartopuntos.activities.ActivityPlantillas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Activity_usuario : AppCompatActivity() {

    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvEmailUsuario: TextView
    private lateinit var tvTotalPartidas: TextView
    private lateinit var btnPlantillas: Button
    private lateinit var btnLogout: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var btnCambiarJuego: Button
    private lateinit var recyclerView: RecyclerView

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val musService = MusService()
    private val magicService = MagicService()

    private var mostrandoMus = true

    private val listaPartidasMus = mutableListOf<PartidaMus>()
    private val listaPartidasMagic = mutableListOf<JuegoMagic>() // CORREGIDO

    private lateinit var musAdapter: PartidaAdapter
    private lateinit var magicAdapter: PartidaMagicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_usuario)

        // Vincular vistas
        tvNombreUsuario = findViewById(R.id.tv_nombreUsuario)
        tvEmailUsuario = findViewById(R.id.tv_emailUsuario)
        tvTotalPartidas = findViewById(R.id.tv_totalPartidas)
        btnPlantillas = findViewById(R.id.btn_plantillas)
        btnLogout = findViewById(R.id.imb_logout)
        btnBack = findViewById(R.id.imb_atras)
        btnCambiarJuego = findViewById(R.id.btn_cambiarJuego)
        recyclerView = findViewById(R.id.rv_partidas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adaptadores
        musAdapter = PartidaAdapter(listaPartidasMus) { partidaEliminada ->
            listaPartidasMus.remove(partidaEliminada)
            musAdapter.notifyDataSetChanged()
            actualizarContador()
        }

        magicAdapter = PartidaMagicAdapter(listaPartidasMagic) { partidaEliminada ->
            listaPartidasMagic.remove(partidaEliminada)
            magicAdapter.notifyDataSetChanged()
            actualizarContador()
        }

        recyclerView.adapter = musAdapter // Por defecto mostrar Mus

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
                    Log.e("ActivityUsuario", "Error al consultar Firestore", it)
                }

            cargarPartidasMus() // Por defecto cargar MUS
        } else {
            // Redirigir si no hay usuario logueado
            startActivity(Intent(this, IniciarSesionActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        btnCambiarJuego.setOnClickListener {
            mostrandoMus = !mostrandoMus
            if (mostrandoMus) {
                recyclerView.adapter = musAdapter
                btnCambiarJuego.text = "Ver Magic"
                cargarPartidasMus()
            } else {
                recyclerView.adapter = magicAdapter
                btnCambiarJuego.text = "Ver Mus"
                cargarPartidasMagic()
            }
        }

        btnPlantillas.setOnClickListener {
            startActivity(Intent(this, ActivityPlantillas::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, IniciarSesionActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun actualizarContador() {
        val total = if (mostrandoMus) listaPartidasMus.size else listaPartidasMagic.size
        tvTotalPartidas.text = "Total de partidas: $total"
    }

    private fun cargarPartidasMus() {
        musService.obtenerPartidasDelUsuario(
            onSuccess = { partidas ->
                listaPartidasMus.clear()
                listaPartidasMus.addAll(partidas)
                musAdapter.notifyDataSetChanged()
                actualizarContador()
            },
            onFailure = {
                Toast.makeText(this, "Error al cargar partidas de Mus", Toast.LENGTH_SHORT).show()
                Log.e("ActivityUsuario", "Error al obtener partidas Mus", it)
            }
        )
    }

    private fun cargarPartidasMagic() {
        magicService.obtenerPartidasDelUsuario(
            onSuccess = { partidas ->
                listaPartidasMagic.clear()
                listaPartidasMagic.addAll(partidas)
                magicAdapter.notifyDataSetChanged()
                actualizarContador()
            },
            onFailure = {
                Toast.makeText(this, "Error al cargar partidas de Magic", Toast.LENGTH_SHORT).show()
                Log.e("ActivityUsuario", "Error al obtener partidas Magic", it)
            }
        )
    }
}
