package com.example.cartopuntos.Model.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cartopuntos.Model.Entity.PartidaMus
import com.example.cartopuntos.Model.Service.MusService
import com.example.cartopuntos.R

class PartidaAdapter(
    private val partidas: MutableList<PartidaMus>, // Lista de partidas
    private val onPartidaBorrada: (PartidaMus) -> Unit // Callback para eliminar una partida
) : RecyclerView.Adapter<PartidaAdapter.PartidaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_partida, parent, false)
        return PartidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartidaViewHolder, position: Int) {
        val partida = partidas[position]
        holder.bind(partida)
    }

    override fun getItemCount(): Int = partidas.size

    fun removePartida(partida: PartidaMus) {
        val position = partidas.indexOf(partida)
        if (position >= 0) {
            partidas.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class PartidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombrePartidaTextView: TextView = itemView.findViewById(R.id.tv_tituloPartida)
        private val btnBorrar: Button = itemView.findViewById(R.id.btn_borrar) // Cambié el nombre del botón
        private val btnRetomar: Button = itemView.findViewById(R.id.btn_retomar) // Cambié el nombre del botón

        fun bind(partida: PartidaMus) {
            nombrePartidaTextView.text = partida.nombrePartida

            // Botón para borrar la partida
            btnBorrar.setOnClickListener {
                borrarPartida(partida)
            }

            // Botón para retomar (por ahora no hace nada)
            btnRetomar.setOnClickListener {
                // Aquí podemos agregar la lógica para retomar la partida cuando esté lista.
                Toast.makeText(itemView.context, "Retomar partida aún no implementado", Toast.LENGTH_SHORT).show()
            }
        }

        private fun borrarPartida(partida: PartidaMus) {
            // Borrar de Firestore
            MusService().eliminarPartida(partida,
                onSuccess = {
                    removePartida(partida) // Eliminar de la lista
                    onPartidaBorrada(partida) // Callback para actualizar la UI
                    Toast.makeText(itemView.context, "Partida eliminada", Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    Toast.makeText(itemView.context, "Error al eliminar partida", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
