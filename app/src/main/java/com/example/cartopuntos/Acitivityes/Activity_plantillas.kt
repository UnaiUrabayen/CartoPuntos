package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartopuntos.R


class Activity_plantillas : AppCompatActivity() {

    private lateinit var recyclerViewPlantillas: RecyclerView
    private lateinit var btnCrearPlantilla: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_plantillas)

        // Inicializar RecyclerView
        recyclerViewPlantillas = findViewById(R.id.recyclerViewPlantillas)
        recyclerViewPlantillas.layoutManager = LinearLayoutManager(this)

        // Inicializar el botón de crear plantilla
        btnCrearPlantilla = findViewById(R.id.btnCrearPlantilla)

        // Aquí aún no agregamos funcionalidad, solo se está configurando el RecyclerView
        btnCrearPlantilla.setOnClickListener {
            val intent = Intent(this, CrearPlantillaActivity::class.java)
            startActivity(intent)
        }
    }
}