package com.example.cartopuntos.model.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.cartopuntos.Acitivityes.Activity_mus
import com.example.cartopuntos.Model.Entity.PlantillaPerfil
import com.example.cartopuntos.R
import com.google.firebase.firestore.FirebaseFirestore

class PlantillasAdapter(
    private val context: Context,
    private val plantillas: MutableList<PlantillaPerfil>,  // mutable para poder eliminar
    private val isDesdeJuego: Boolean  // Indica si se debe mostrar el botón "Seleccionar"
) : RecyclerView.Adapter<PlantillasAdapter.PlantillaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantillaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_plantilla, parent, false)
        return PlantillaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantillaViewHolder, position: Int) {
        val plantilla = plantillas[position]

        // Set the title of the template
        holder.tvTituloPlantilla.text = plantilla.nombreJugador

        // Load background image using Glide with placeholder
        Glide.with(context)
            .load(plantilla.urlFondo)
            .placeholder(R.drawable.placeholder_image) // Add a placeholder
            .into(holder.imgFondo)

        // Load profile image using Glide with placeholder
        Glide.with(context)
            .load(plantilla.fotoPerfilUrl)
            .placeholder(R.drawable.account_circle_24px)
            .transform(RoundedCorners(20))
            .circleCrop()  // <-- Esto hace la imagen circular
            .into(holder.imgPerfil)

        // Configurar el botón de eliminar
        holder.btnEliminar.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val plantillaEliminar = plantillas[pos]
                val db = FirebaseFirestore.getInstance()
                val plantillaId = plantillaEliminar.idPlantilla
                val usuarioId = plantillaEliminar.uidUsuario

                // Eliminar la plantilla de Firestore
                db.collection("usuarios")
                    .document(usuarioId)
                    .collection("plantillas")
                    .document(plantillaId)
                    .delete()
                    .addOnSuccessListener {
                        // Actualizar la lista local y notificar al adaptador
                        plantillas.removeAt(pos)
                        notifyItemRemoved(pos)  // Notificar que el elemento fue eliminado
                    }
                    .addOnFailureListener { e ->
                        // Manejar el error
                    }
            }
        }

        // Mostrar/Ocultar el botón de seleccionar dependiendo del origen
        if (isDesdeJuego) {
            holder.btnSeleccionar.visibility = View.VISIBLE
            holder.btnSeleccionar.setOnClickListener {
                if (context is com.example.cartopuntos.activities.ActivityPlantillas) {
                    context.devolverPlantillaSeleccionada(plantilla)
                }
            }


        } else {
            holder.btnSeleccionar.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = plantillas.size

    // ViewHolder class to hold references to UI components
    inner class PlantillaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFondo: ImageView = itemView.findViewById(R.id.imgFondo)
        val imgPerfil: ImageView = itemView.findViewById(R.id.imgPerfil)
        val tvTituloPlantilla: TextView = itemView.findViewById(R.id.tvTituloPlantilla)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
        val btnSeleccionar: Button = itemView.findViewById(R.id.btnSeleccionar)  // Nuevo botón para seleccionar
    }
}
