package com.example.cartopuntos.Dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.R
import com.example.cartopuntos.Acitivityes.Activity_magic

class MenuDialogReiniciarMagic(
    private val jugadores: List<String>,
    private val onReiniciarClick: () -> Unit,
    private val onDeclararClick: (ganador: Int) -> Unit
) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.menu_reiniciar_mus, null)

        // Encontrar los botones
        val btnVolver = view.findViewById<View>(R.id.btnVolver)
        val btnReiniciar = view.findViewById<View>(R.id.btnReiniciar)
        val btnDeclarar = view.findViewById<View>(R.id.btnDeclarar)

        // Acción para volver (cerrar el diálogo)
        btnVolver.setOnClickListener { dismiss() }

        // Acción para reiniciar el juego
        btnReiniciar.setOnClickListener {
            onReiniciarClick()  // Llamada a la función para reiniciar el juego
            dismiss()
        }

        // Acción para declarar un ganador (esto depende de cómo quieras manejar los ganadores en Magic)
        btnDeclarar.setOnClickListener {
            var jugadorSeleccionado = -1

            AlertDialog.Builder(requireContext())
                .setTitle("Selecciona el ganador")
                .setSingleChoiceItems(jugadores.toTypedArray(), -1) { _, which ->
                    jugadorSeleccionado = which
                }
                .setPositiveButton("Aceptar") { _, _ ->
                    if (jugadorSeleccionado != -1) {
                        mostrarDialogoGuardar(jugadorSeleccionado + 1)  // Sumar 1 si los IDs empiezan desde 1
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }


        // Crear el diálogo con el diseño proporcionado
        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
    private fun mostrarDialogoGuardar(jugadorGanador: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Jugador $jugadorGanador ha ganado")
            .setMessage("¿Deseas guardar la partida?")
            .setPositiveButton("Sí") { _, _ ->
                Toast.makeText(requireContext(), "Guardando partida...", Toast.LENGTH_SHORT).show()
                onDeclararClick(jugadorGanador)
            }
            .setNegativeButton("No") { _, _ ->
                Toast.makeText(requireContext(), "Partida no guardada", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

}
