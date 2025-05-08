package com.example.cartopuntos.Dialogs

import JugadorMagic
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.Acitivityes.Activity_magic
import com.example.cartopuntos.R

class MenuContadoresDialog(
    private val jugador: JugadorMagic,
    private val listaJugadores: List<JugadorMagic>,
    private val onContadoresActualizados: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_menu_contadores, null)

        val venenoTV = view.findViewById<TextView>(R.id.tv_veneno)
        val energiaTV = view.findViewById<TextView>(R.id.tv_energia)
        val tesorosTV = view.findViewById<TextView>(R.id.tv_tesoros)
        val masUnoTV = view.findViewById<TextView>(R.id.tv_mas_uno)
        val monarcaCB = view.findViewById<CheckBox>(R.id.cb_monarca)
        val iniciativaCB = view.findViewById<CheckBox>(R.id.cb_iniciativa)
        val btnDanoComandante = view.findViewById<Button>(R.id.btn_dano_comandante)

        venenoTV.text = jugador.contadorVeneno.toString()
        energiaTV.text = jugador.contadorEnergia.toString()
        tesorosTV.text = jugador.contadorTesoros.toString()
        masUnoTV.text = jugador.contadorMasUno.toString()
        monarcaCB.isChecked = jugador.esMonarca
        iniciativaCB.isChecked = jugador.tieneIniciativa

        fun setupCounter(
            label: TextView,
            getter: () -> Int,
            setter: (Int) -> Unit,
            addBtn: Int,
            subBtn: Int
        ) {
            view.findViewById<Button>(addBtn).setOnClickListener {
                setter(getter() + 1)
                label.text = getter().toString()
            }
            view.findViewById<Button>(subBtn).setOnClickListener {
                if (getter() > 0) {
                    setter(getter() - 1)
                    label.text = getter().toString()
                }
            }
        }

        setupCounter(
            venenoTV,
            { jugador.contadorVeneno },
            { jugador.contadorVeneno = it },
            R.id.btn_veneno_mas,
            R.id.btn_veneno_menos
        )
        setupCounter(
            energiaTV,
            { jugador.contadorEnergia },
            { jugador.contadorEnergia = it },
            R.id.btn_energia_mas,
            R.id.btn_energia_menos
        )
        setupCounter(
            tesorosTV,
            { jugador.contadorTesoros },
            { jugador.contadorTesoros = it },
            R.id.btn_tesoros_mas,
            R.id.btn_tesoros_menos
        )
        setupCounter(
            masUnoTV,
            { jugador.contadorMasUno },
            { jugador.contadorMasUno = it },
            R.id.btn_mas_uno_mas,
            R.id.btn_mas_uno_menos
        )

        val estadoDiaNocheTV = view.findViewById<TextView>(R.id.tv_estado_dia_noche)
        val btnDiaNoche = view.findViewById<Button>(R.id.btn_toggle_dia_noche)
        var esDia = jugador.esDia
        estadoDiaNocheTV.text = if (esDia) "Día" else "Noche"

        btnDiaNoche.setOnClickListener {
            esDia = !esDia
            estadoDiaNocheTV.text = if (esDia) "Día" else "Noche"
            jugador.esDia = esDia
        }

        monarcaCB.setOnCheckedChangeListener { _, isChecked ->
            jugador.esMonarca = isChecked
        }

        iniciativaCB.setOnCheckedChangeListener { _, isChecked ->
            jugador.tieneIniciativa = isChecked
        }

        btnDanoComandante.setOnClickListener {
            val dialog = DialogoComandanteDamage(
                jugadorActual = jugador,
                jugadores = listaJugadores,
                juego = (activity as Activity_magic).juego
            )
            dialog.show(parentFragmentManager, "DialogoComandanteDamage")
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton("Guardar") { dialog, _ ->
                // Verificar si el jugador muere por daño de comandante o veneno
                jugador.actualizarEstadoDeMuerte()

                if (jugador.vida <= 0) {
                    Toast.makeText(requireContext(), "${jugador.nombre} ha muerto", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Contadores guardados", Toast.LENGTH_SHORT).show()
                }

                onContadoresActualizados()
                dialog.dismiss()
            }
            .setNegativeButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}
