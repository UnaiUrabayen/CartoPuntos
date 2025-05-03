package com.example.cartopuntos.Dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.R

class MenuDialogFragment(
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
            onDeclararClick()
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

    }

}
