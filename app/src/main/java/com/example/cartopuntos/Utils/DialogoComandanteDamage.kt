package com.example.cartopuntos.Dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.Logica.JuegoMagic
import com.example.cartopuntos.R

class DialogoComandanteDamage(
    private val jugadorId: Int,
    private val cantidadJugadores: Int,
    private val juego: JuegoMagic
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialogo_comandante_damage, null)
        val contenedor = view.findViewById<LinearLayout>(R.id.containerComandantes)

        for (i in 1..cantidadJugadores) {
            if (i == jugadorId) continue  // No debe hacerse daño a sí mismo

            val subView = inflater.inflate(R.layout.item_comandante_damage, null)
            val tvNombre = subView.findViewById<TextView>(R.id.tvComandanteNombre)
            val etDamage = subView.findViewById<EditText>(R.id.etDamage)

            tvNombre.text = "Comandante $i"
            etDamage.setText(juego.obtenerDañoDeComandante(jugadorId, i).toString())

            contenedor.addView(subView)
        }

        builder.setView(view)
            .setTitle("Daño por comandante de Jugador $jugadorId")
            .setPositiveButton("Guardar") { _, _ ->
                for (i in 0 until contenedor.childCount) {
                    val item = contenedor.getChildAt(i)
                    val nombreText = item.findViewById<TextView>(R.id.tvComandanteNombre).text.toString()
                    val comandanteId = nombreText.split(" ").last().toInt()
                    val etDamage = item.findViewById<EditText>(R.id.etDamage)
                    val nuevoValor = etDamage.text.toString().toIntOrNull() ?: 0
                    juego.infligirDañoPorComandante(jugadorId, comandanteId, nuevoValor - juego.obtenerDañoDeComandante(jugadorId, comandanteId))
                }
                Toast.makeText(requireContext(), "Daño actualizado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)

        return builder.create()
    }
}
