package com.example.cartopuntos.Model.Entity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cartopuntos.R
import com.google.firebase.firestore.FirebaseFirestore

class PlantillasAdapter(
    private val context: Context,
    private val plantillas: MutableList<PlantillaPerfil>  // mutable para poder eliminar
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
            .placeholder(R.drawable.placeholder_image) // Add a placeholder
            .into(holder.imgPerfil)

        // Set click listener for the delete button
        holder.btnEliminar.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val plantillaEliminar = plantillas[pos]
                val db = FirebaseFirestore.getInstance()
                val plantillaId = plantillaEliminar.idPlantilla
                val usuarioId = plantillaEliminar.uidUsuario

                // Delete the plantilla from Firestore
                db.collection("usuarios")
                    .document(usuarioId)
                    .collection("plantillas")
                    .document(plantillaId)
                    .delete()
                    .addOnSuccessListener {
                        // Update the local list and notify the adapter
                        plantillas.removeAt(pos)
                        notifyItemRemoved(pos)  // Notify the removal of the item
                    }
                    .addOnFailureListener { e ->
                        Log.e("PlantillasAdapter", "Error eliminando plantilla: ", e)
                    }
            }
        }
    }

    override fun getItemCount(): Int = plantillas.size

    // ViewHolder class to hold references to UI components
    inner class PlantillaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFondo: ImageView = itemView.findViewById(R.id.imgFondo)
        val imgPerfil: ImageView = itemView.findViewById(R.id.imgPerfil)
        val tvTituloPlantilla: TextView = itemView.findViewById(R.id.tvTituloPlantilla)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }
}
