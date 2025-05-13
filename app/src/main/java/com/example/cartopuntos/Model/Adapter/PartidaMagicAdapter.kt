package com.example.cartopuntos.Model.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cartopuntos.Acitivityes.Activity_magic
import com.example.cartopuntos.Logica.JuegoMagic
import com.example.cartopuntos.Model.Service.MagicService
import com.example.cartopuntos.R
import java.text.SimpleDateFormat
import java.util.*

class PartidaMagicAdapter(
    private val partidas: MutableList<JuegoMagic>,
    private val onPartidaBorrada: (JuegoMagic) -> Unit
) : RecyclerView.Adapter<PartidaMagicAdapter.PartidaMagicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidaMagicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_partida, parent, false)
        return PartidaMagicViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartidaMagicViewHolder, position: Int) {
        val partida = partidas[position]
        holder.bind(partida)
    }

    override fun getItemCount(): Int = partidas.size

    fun removePartida(partida: JuegoMagic) {
        val position = partidas.indexOf(partida)
        if (position >= 0) {
            partidas.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class PartidaMagicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tv_tituloPartida)
        private val tvFecha: TextView = itemView.findViewById(R.id.tv_fecha)
        private val btnBorrar: Button = itemView.findViewById(R.id.btn_borrar)
        private val btnRetomar: Button = itemView.findViewById(R.id.btn_retomar)

        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun bind(partida: JuegoMagic) {
            tvNombre.text = partida.nombrePartida

            val fecha = Date(partida.fecha.toLong())
            tvFecha.text = dateFormat.format(fecha)

            btnBorrar.setOnClickListener {
                borrarPartida(partida)
            }

            btnRetomar.setOnClickListener {
                val context = itemView.context
                val idPartida = partida.id

                if (!idPartida.isNullOrEmpty()) {
                    val intent = Intent(context, Activity_magic::class.java)
                    intent.putExtra("id_partida", idPartida)
                    context.startActivity(intent)
                    Log.d("PartidaMagicAdapter", "Intent enviado a Activity_magic con ID: $idPartida")
                } else {
                    Log.e("PartidaMagicAdapter", "ID de partida es null o vacío")
                    Toast.makeText(context, "Error: ID de partida inválido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun borrarPartida(partida: JuegoMagic) {
            Log.d("PartidaMagicAdapter", "Borrando partida: ${partida.nombrePartida}")

            MagicService().eliminarPartida(
                partida,
                onSuccess = {
                    Log.d("PartidaMagicAdapter", "Partida eliminada: ${partida.id}")
                    removePartida(partida)
                    onPartidaBorrada(partida)
                    Toast.makeText(itemView.context, "Partida eliminada", Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    Log.e("PartidaMagicAdapter", "Error al eliminar partida: ${partida.id}")
                    Toast.makeText(itemView.context, "Error al eliminar partida", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
