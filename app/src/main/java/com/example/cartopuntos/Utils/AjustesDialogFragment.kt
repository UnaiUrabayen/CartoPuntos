package com.example.cartopuntos.Utils

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.R

class AjustesDialogFragment(
    private val onRondasChanged: (Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.menu_ajustes_mus, null)

        // Número de rondas (NumberPicker)
        val numberPicker = view.findViewById<NumberPicker>(R.id.numberPickerRondas)
        numberPicker.minValue = 1    // Configuramos el valor mínimo
        numberPicker.maxValue = 10   // Configuramos el valor máximo
        numberPicker.value = 3       // Valor inicial

        // Botón de volver
        val btnVolver = view.findViewById<ImageButton>(R.id.btnVolver)
        btnVolver.setOnClickListener { dismiss() }

        // Botón de confirmar cambios
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmar)
        btnConfirmar.setOnClickListener {
            val rondas = numberPicker.value
            onRondasChanged(rondas)  // Llamamos a la función que pasa el valor
            dismiss()  // Cerrar el diálogo
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}
