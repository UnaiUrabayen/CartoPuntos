package com.example.cartopuntos.Dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.R

class MenuDialogDados(
    private val onDadoSeleccionado: (caras: Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.menu_dados, null)

        val ids = listOf(
            R.id.btn_d2 to 2,
            R.id.btn_d3 to 3,
            R.id.btn_d4 to 4,
            R.id.btn_d6 to 6,
            R.id.btn_d8 to 8,
            R.id.btn_d10 to 10,
            R.id.btn_d12 to 12,
            R.id.btn_d16 to 16,
            R.id.btn_d20 to 20,
            R.id.btn_dX to -1  // Personalizado
        )

        ids.forEach { (id, caras) ->
            view.findViewById<Button>(id)?.setOnClickListener {
                if (caras == -1) {
                    pedirNumeroPersonalizado()
                } else {
                    onDadoSeleccionado(caras)
                    dismiss()
                }
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Menú de Dados")
            .setView(view)
            .create()
    }

    private fun pedirNumeroPersonalizado() {
        val inputDialog = CustomDadoDialog { personalizado ->
            onDadoSeleccionado(personalizado)
            dismiss()
        }
        inputDialog.show(parentFragmentManager, "CustomDadoDialog")
    }
}
