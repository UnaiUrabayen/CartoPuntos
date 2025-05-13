import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cartopuntos.Logica.JuegoMagic
import com.example.cartopuntos.Model.Service.MagicService
import com.example.cartopuntos.R

class MenuDialogReiniciarMagic(
    private val jugadores: List<String>,
    private val partida: JuegoMagic,
    private val onReiniciarClick: () -> Unit
) : DialogFragment() {

    private val service = MagicService()

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
            mostrarDialogoGuardarPartida()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    private fun mostrarDialogoGuardarPartida() {
        val input = EditText(requireContext())
        input.hint = "Nombre de la partida"

        AlertDialog.Builder(requireContext())
            .setTitle("Guardar partida")
            .setMessage("Introduce un nombre para la partida:")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = input.text.toString().trim()
                if (nombre.isNotEmpty()) {
                    partida.nombrePartida = nombre
                    partida.fecha = System.currentTimeMillis().toString()

                    service.guardarPartida(partida,
                        onSuccess = {
                            Toast.makeText(requireContext(), "Partida guardada con éxito", Toast.LENGTH_SHORT).show()
                            dismiss()
                        },
                        onFailure = {
                            Toast.makeText(requireContext(), "Error al guardar la partida", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
