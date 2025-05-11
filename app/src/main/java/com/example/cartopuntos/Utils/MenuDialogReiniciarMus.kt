package com.example.cartopuntos.Utils

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.Model.Entity.PartidaMus
import com.example.cartopuntos.Model.Service.MusService
import com.example.cartopuntos.R

class MenuDialogReiniciarMus(
    private val onReiniciarClick: () -> Unit,
    private val onDeclararClick: () -> Unit,
    private val partida: PartidaMus
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
            mostrarDialogoGuardar()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    private fun mostrarDialogoGuardar() {
        val input = EditText(requireContext())
        input.hint = "Nombre de la partida"

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Guardar partida")
            .setMessage("¿Deseas guardar la partida actual?")
            .setView(input)  // Establece el campo de texto dentro del diálogo
            .setPositiveButton("Sí") { _, _ ->
                val nombrePartida = input.text.toString().trim()
                if (nombrePartida.isNotEmpty()) {
                    partida.nombrePartida = nombrePartida // Asignamos el nombre a la partida

                    val service = MusService()
                    service.guardarPartida(partida,
                        onSuccess = {
                            Toast.makeText(requireContext(), "Partida guardada con éxito", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = {
                            Toast.makeText(requireContext(), "Error al guardar partida", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(requireContext(), "El nombre de la partida no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No") { _, _ ->
                Toast.makeText(requireContext(), "Partida no guardada", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}

