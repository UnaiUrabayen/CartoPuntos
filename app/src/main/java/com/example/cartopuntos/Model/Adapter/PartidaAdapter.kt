
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
import com.example.cartopuntos.Acitivityes.Activity_mus
import com.example.cartopuntos.Model.Entity.PartidaMus
import com.example.cartopuntos.Model.Service.MusService
import com.example.cartopuntos.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PartidaAdapter(
    private val partidas: MutableList<PartidaMus>, // Lista de partidas
    private val onPartidaBorrada: (PartidaMus) -> Unit // Callback para eliminar una partida
) : RecyclerView.Adapter<PartidaAdapter.PartidaViewHolder>() {

    // Inicializar Firestore
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
        private val fechaPartidaTextView: TextView = itemView.findViewById(R.id.tv_fecha) // Nueva TextView para la fecha
        private val btnBorrar: Button = itemView.findViewById(R.id.btn_borrar)
        private val btnRetomar: Button = itemView.findViewById(R.id.btn_retomar)

        // Formato de la fecha
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun bind(partida: PartidaMus) {
            nombrePartidaTextView.text = partida.nombrePartida

            // Convertir timestamp a una fecha legible
            val fecha = Date(partida.fecha.toLong())  // Convertir el timestamp a un objeto Date
            val fechaFormateada = dateFormat.format(fecha)  // Formatear la fecha

            fechaPartidaTextView.text = fechaFormateada  // Establecer la fecha formateada en el TextView

            btnBorrar.setOnClickListener {
                borrarPartida(partida)
            }

            btnRetomar.setOnClickListener {
                // Log para verificar que el item fue clickeado
                Log.d("PartidaAdapter", "Item clickeado: " + partida.nombrePartida)

                // Obtener el contexto y verificar si partida no es null
                val context = itemView.context
                val idPartida = partida.id  // Asegúrate de que 'id' no sea null
                if (idPartida != null) {
                    // Consultar Firestore por el ID de la partida
                    db.collection("partidasMus")
                        .document(idPartida)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val partida = document.toObject(PartidaMus::class.java)
                                if (partida != null) {
                                    // Aquí puedes hacer algo con la partida, como mostrarla
                                    Log.d("ActivityUsuario", "Partida obtenida: ${partida.nombrePartida}")
                                }
                            } else {
                                Log.e("ActivityUsuario", "No se encontró la partida con el ID: $idPartida")
                            }
                        }
                        .addOnFailureListener {
                            Log.e("ActivityUsuario", "Error al obtener la partida")
                        }

                    // Enviar la partida a la siguiente actividad (Activity_mus)
                    val intent = Intent(context, Activity_mus::class.java)
                    intent.putExtra("id_partida", idPartida) // idPartida es el valor que estás pasando

                    // Verificar que el ID es correcto antes de iniciar la actividad
                    if (idPartida.isNotEmpty()) {
                        context.startActivity(intent)
                        Log.d("PartidaAdapter", "Intent enviado a Activity_mus con ID de partida: $idPartida")
                    } else {
                        Log.e("PartidaAdapter", "El ID de la partida está vacío, no se puede iniciar Activity_mus.")
                        Toast.makeText(context, "Error: ID de partida vacío", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("PartidaAdapter", "El ID de la partida es null, no se puede iniciar Activity_mus.")
                    Toast.makeText(context, "Error: ID de partida es null", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun borrarPartida(partida: PartidaMus) {
            // Log para verificar que el proceso de borrado fue iniciado
            Log.d("PartidaAdapter", "Borrando partida: " + partida.nombrePartida)

            // Borrar de Firestore
            MusService().eliminarPartida(partida,
                onSuccess = {
                    // Log para confirmar que la partida fue eliminada exitosamente
                    Log.d("PartidaAdapter", "Partida eliminada: " + partida.nombrePartida)
                    removePartida(partida) // Eliminar de la lista
                    onPartidaBorrada(partida) // Callback para actualizar la UI
                    Toast.makeText(itemView.context, "Partida eliminada", Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    // Log para manejar el error en el proceso de eliminación
                    Log.e("PartidaAdapter", "Error al eliminar partida: " + partida.nombrePartida)
                    Toast.makeText(itemView.context, "Error al eliminar partida", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
