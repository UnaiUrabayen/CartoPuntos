package com.example.cartopuntos.Acitivityes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartopuntos.Model.Entity.PlantillaPerfil
import com.example.cartopuntos.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

class CrearPlantillaActivity : AppCompatActivity() {

    private lateinit var imageViewPerfil: ImageView
    private lateinit var imageViewFondo: ImageView
    private lateinit var btnElegirFotoPerfil: Button
    private lateinit var btnElegirFondo: Button
    private lateinit var btnGuardarPerfil: Button
    private lateinit var etNombreJugador: EditText
    private lateinit var progressBar: ProgressBar

    private var imageUriPerfil: Uri? = null
    private var imageUriFondo: Uri? = null

    private val clientId = "a52211ddb770dbd"
    private val REQUEST_CODE_PROFILE = 100
    private val REQUEST_CODE_BACKGROUND = 200

    private var urlPerfilSubida: String? = null
    private var urlFondoSubida: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crear_plantilla)

        imageViewPerfil = findViewById(R.id.imgVistaPreviaPerfil)
        imageViewFondo = findViewById(R.id.imgVistaPreviaFondo)
        btnElegirFotoPerfil = findViewById(R.id.btnElegirFotoPerfil)
        btnElegirFondo = findViewById(R.id.btnElegirFondo)
        btnGuardarPerfil = findViewById(R.id.btnGuardarPerfil)
        etNombreJugador = findViewById(R.id.etNombreJugador)
        progressBar = findViewById(R.id.progressBar)

        btnElegirFotoPerfil.setOnClickListener {
            openGallery(REQUEST_CODE_PROFILE)
        }

        btnElegirFondo.setOnClickListener {
            openGallery(REQUEST_CODE_BACKGROUND)
        }

        btnGuardarPerfil.setOnClickListener {
            val nombreJugador = etNombreJugador.text.toString()
            if (nombreJugador.isEmpty() || imageUriPerfil == null || imageUriFondo == null) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage(imageUriPerfil!!, "profile", nombreJugador)
                uploadImage(imageUriFondo!!, "background", nombreJugador)
            }
        }
    }

    private fun openGallery(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data!!
            if (requestCode == REQUEST_CODE_PROFILE) {
                imageUriPerfil = selectedImageUri
                imageViewPerfil.setImageURI(selectedImageUri)
            } else if (requestCode == REQUEST_CODE_BACKGROUND) {
                imageUriFondo = selectedImageUri
                imageViewFondo.setImageURI(selectedImageUri)
            }
        }
    }

    private fun uploadImage(uri: Uri, type: String, nombreJugador: String) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // Mostrar el ProgressBar
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }

        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", "image.jpg", RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray))
            .build()

        val request = Request.Builder()
            .url("https://api.imgur.com/3/image")
            .addHeader("Authorization", "Client-ID $clientId")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val json = JSONObject(responseData)
                    val imageUrl = json.getJSONObject("data").getString("link")

                    synchronized(this@CrearPlantillaActivity) {
                        if (type == "profile") {
                            urlPerfilSubida = imageUrl
                        } else if (type == "background") {
                            urlFondoSubida = imageUrl
                        }

                        if (urlPerfilSubida != null && urlFondoSubida != null) {
                            guardarPlantillaEnFirestore(nombreJugador, urlPerfilSubida!!, urlFondoSubida!!)
                            runOnUiThread {
                                // Ocultar el ProgressBar después de la subida
                                progressBar.visibility = View.GONE
                                Toast.makeText(this@CrearPlantillaActivity, "Perfil guardado exitosamente", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        progressBar.visibility = View.GONE // Ocultar el ProgressBar si hay un error
                        Toast.makeText(this@CrearPlantillaActivity, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    progressBar.visibility = View.GONE // Ocultar el ProgressBar si hay un error de conexión
                    Toast.makeText(this@CrearPlantillaActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun guardarPlantillaEnFirestore(nombreJugador: String, urlPerfil: String, urlFondo: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uidUsuario = currentUser?.uid ?: return

        val db = FirebaseFirestore.getInstance()
        val plantillasRef = db.collection("usuarios").document(uidUsuario).collection("plantillas")

        val idPlantilla = plantillasRef.document().id // genera un ID único

        val plantilla = PlantillaPerfil(
            idPlantilla = idPlantilla,
            uidUsuario = uidUsuario,
            nombreJugador = nombreJugador,
            fotoPerfilUrl = urlPerfil,
            urlFondo = urlFondo
        )

        plantillasRef.document(idPlantilla)
            .set(plantilla)
            .addOnSuccessListener {
                runOnUiThread {
                    Toast.makeText(this, "Plantilla guardada correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
            .addOnFailureListener {
                runOnUiThread {
                    Toast.makeText(this, "Error al guardar la plantilla", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
