package com.example.cartopuntos.Utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.R

class ConfiguracionMagicDialog(
    val onAplicar: (jugadores: Int, sonido: Boolean, vidaNeg: Boolean, autoReset: Boolean) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_config_magic, null)

        val spinner = view.findViewById<Spinner>(R.id.spinnerCantidadJugadores)
        val cbSonido = view.findViewById<CheckBox>(R.id.checkboxSonido)
        val cbVidaNeg = view.findViewById<CheckBox>(R.id.checkboxVidaNegativa)
        val cbAutoReset = view.findViewById<CheckBox>(R.id.checkboxAutoReset)
        val descriptionText = view.findViewById<TextView>(R.id.descriptionText)

        // Configurar el texto adicional del dialogo
        descriptionText.text = "Puedes cambiar la cantidad de jugadores."

        spinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf(2, 3, 4, 6, 8)
        )

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()

        view.findViewById<Button>(R.id.btnAplicar).setOnClickListener {
            val cantidad = spinner.selectedItem as Int
            val sonido = cbSonido.isChecked
            val vidaNeg = cbVidaNeg.isChecked
            val autoReset = cbAutoReset.isChecked

            // Asegurarse de pasar la cantidad de jugadores junto con las otras configuraciones
            onAplicar(cantidad, sonido, vidaNeg, autoReset)
            dialog.dismiss()
        }

        return dialog
    }
}
