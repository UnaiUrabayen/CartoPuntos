package com.example.cartopuntos.Utils

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.R

class MenuDialogReiniciarMus(
    private val onReiniciarClick: () -> Unit,
    private val onDeclararClick: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.menu_reiniciar_mus, null)

        val btnVolver = view.findViewById<View>(R.id.btnVolver)
        val btnReiniciar = view.findViewById<View>(R.id.btnReiniciar)
        val btnDeclarar = view.findViewById<View>(R.id.btnDeclarar)

        btnVolver.setOnClickListener { dismiss() }
        btnReiniciar.setOnClickListener {
            onReiniciarClick()
            dismiss()
        }
        btnDeclarar.setOnClickListener {
            val jugadores = arrayOf("Jugador 1", "Jugador 2", "Jugador 3", "Jugador 4") // Esto debería generarse dinámicamente
            var jugadorSeleccionado = -1

            AlertDialog.Builder(requireContext())
                .setTitle("Selecciona el ganador")
                .setSingleChoiceItems(jugadores, -1) { _, which ->
                    jugadorSeleccionado = which + 1
                }
                .setPositiveButton("Aceptar") { _, _ ->
                    if (jugadorSeleccionado != -1) {
                        mostrarDialogoGuardar(jugadorSeleccionado)
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }


        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

    }
    private fun mostrarDialogoGuardar(jugadorGanador: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Jugador $jugadorGanador ha ganado")
            .setMessage("¿Deseas guardar la partida?")
            .setPositiveButton("Sí") { _, _ ->
                // Aquí iría la lógica de guardar la partida
                Toast.makeText(requireContext(), "Guardando partida...", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { _, _ ->
                Toast.makeText(requireContext(), "Partida no guardada", Toast.LENGTH_SHORT).show()
            }
            .show()
    }


}
