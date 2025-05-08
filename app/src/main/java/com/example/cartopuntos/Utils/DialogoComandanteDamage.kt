package com.example.cartopuntos.Dialogs

import JugadorMagic
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.Logica.JuegoMagic

import com.example.cartopuntos.R
class DialogoComandanteDamage(
    private val jugadorActual: JugadorMagic,
    private val jugadores: List<JugadorMagic>,
    private val juego: JuegoMagic
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialogo_comandante_damage, null)
        val contenedor = view.findViewById<LinearLayout>(R.id.containerComandantes)

        for (jugador in jugadores) {
            if (jugador.id == jugadorActual.id) continue

            val subView = inflater.inflate(R.layout.item_comandante_damage, null)
            val tvNombre = subView.findViewById<TextView>(R.id.tvComandanteNombre)
            val etDamage = subView.findViewById<EditText>(R.id.etDamage)

            tvNombre.text = jugador.nombre
            etDamage.setText(juego.obtenerDañoDeComandante(jugadorActual.id, jugador.id).toString())

            contenedor.addView(subView)
        }

        builder.setView(view)
            .setTitle("Daño recibido por ${jugadorActual.nombre}")
            .setPositiveButton("Guardar") { _, _ ->
                for (i in 0 until contenedor.childCount) {
                    val item = contenedor.getChildAt(i)
                    val nombreJugador = item.findViewById<TextView>(R.id.tvComandanteNombre).text.toString()
                    val jugadorAtacante = jugadores.find { it.nombre == nombreJugador }
                    val etDamage = item.findViewById<EditText>(R.id.etDamage)
                    val nuevoValor = etDamage.text.toString().toIntOrNull() ?: 0

                    jugadorAtacante?.let {
                        val actual = juego.obtenerDañoDeComandante(jugadorActual.id, it.id)
                        val diferencia = nuevoValor - actual
                        juego.infligirDañoPorComandante(jugadorActual.id, it.id, diferencia)
                    }
                }

                // Actualizamos el estado de muerte de todos los jugadores, para comprobar si alguien muere
                for (jugador in jugadores) {
                    jugador.actualizarEstadoDeMuerte()
                    if (jugador.vida == 0) {
                        Toast.makeText(requireContext(), "${jugador.nombre} ha muerto", Toast.LENGTH_SHORT).show()
                    }
                }

                Toast.makeText(requireContext(), "Daño actualizado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)

        return builder.create()
    }
}

