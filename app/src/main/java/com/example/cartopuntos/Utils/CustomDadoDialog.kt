package com.example.cartopuntos.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class CustomDadoDialog(
    val onNumeroIngresado: (Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val input = EditText(requireContext())
        input.hint = "Número de caras"

        return AlertDialog.Builder(requireContext())
            .setTitle("Dado Personalizado")
            .setView(input)
            .setPositiveButton("Lanzar") { _, _ ->
                val texto = input.text.toString()
                val numero = texto.toIntOrNull()
                if (numero != null && numero > 0) {
                    onNumeroIngresado(numero)
                } else {
                    Toast.makeText(requireContext(), "Número inválido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}
