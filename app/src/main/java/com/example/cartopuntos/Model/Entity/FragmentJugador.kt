package com.example.cartopuntos.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cartopuntos.R

class FragmentJugador : Fragment() {

    private lateinit var nombreJugador: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jugador_mus, container, false)

        nombreJugador = view.findViewById(R.id.nombreJugador)

        // Puedes recibir argumentos (nombre del jugador, puntos, etc)
        val nombre = arguments?.getString("nombre") ?: "Jugador"

        nombreJugador.text = nombre

        return view
    }

    companion object {
        fun newInstance(nombre: String): FragmentJugador {
            val fragment = FragmentJugador()
            val args = Bundle()
            args.putString("nombre", nombre)
            fragment.arguments = args
            return fragment
        }
    }
}
