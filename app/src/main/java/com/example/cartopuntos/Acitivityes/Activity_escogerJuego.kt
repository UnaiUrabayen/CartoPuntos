package com.example.cartopuntos.Acitivityes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cartopuntos.R

class Activity_escogerJuego : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_escoger_juego) // Asegúrate de que este sea el nombre de tu layout XML

        val btnUsuario: ImageView = findViewById(R.id.imgb_btn_usuario)
        val btnMagic: ImageButton = findViewById(R.id.imb_magic)
        val btnMus: ImageButton = findViewById(R.id.imb_mus)

        btnUsuario.setOnClickListener {
            val intent = Intent(this, Activity_usuario::class.java)
            startActivity(intent)
        }

        btnMagic.setOnClickListener {
            val intent = Intent(this, Activity_magic::class.java)
            startActivity(intent)
        }

        btnMus.setOnClickListener {
            val intent = Intent(this, Activity_mus::class.java)
            startActivity(intent)
        }
        val btnMenu = findViewById<ImageButton>(R.id.btn_menu_info)
        btnMenu.setOnClickListener {
            val popup = PopupMenu(this, btnMenu)
            popup.menuInflater.inflate(R.menu.menu_reglas, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.reglas_mus -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://link-a-reglas-mus"))
                        startActivity(intent)
                        true
                    }
                    R.id.reglas_magic -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://link-a-reglas-magic"))
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
        btnMenu.setOnClickListener {
            val popup = PopupMenu(this, btnMenu)
            popup.menuInflater.inflate(R.menu.menu_reglas, popup.menu)

            // Cambiar color de texto de los ítems
            for (i in 0 until popup.menu.size()) {
                val menuItem = popup.menu.getItem(i)
                val spannableTitle = SpannableString(menuItem.title)
                spannableTitle.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.cp_light_button)),
                    0,
                    spannableTitle.length,
                    0
                )
                menuItem.title = spannableTitle
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.reglas_mus -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nhfournier.es/como-jugar/mus/"))
                        startActivity(intent)
                        true
                    }
                    R.id.reglas_magic -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://magic.wizards.com/es/formats/commander"))
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

    }
}
