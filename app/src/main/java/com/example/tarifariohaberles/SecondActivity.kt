package com.example.tarifariohaberles //mucho muy importante |

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SecondActivity : AppCompatActivity() {

    private lateinit var cliente: EditText
    private lateinit var costoHora: EditText
    private lateinit var totalCobrado: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        cliente = findViewById(R.id.editTextCliente)
        costoHora = findViewById(R.id.editTextCostoHora)
        totalCobrado = findViewById(R.id.editTextTotalCobrado)
    }

    fun Compartir(view: View) {
        val clienteText = cliente.text.toString()
        val costoHoraText = costoHora.text.toString()
        val totalCobradoText = totalCobrado.text.toString()

        try {
            if (clienteText.isNotEmpty() && costoHoraText.isNotEmpty() && totalCobradoText.isNotEmpty()) {
                val totalCobrado: Double = totalCobradoText.toDouble()
                val costoPorHora: Double = costoHoraText.toDouble()

                // Validar que los valores no sean 0 o negativos
                if (costoPorHora <= 0 || totalCobrado <= 0) {
                    mostrarAlerta("LOS CAMPOS COSTO POR HORA Y TOTAL COBRADO DEBEN SER MAYORES QUE 0")
                    return
                }

                val totalHorasTrabajadas = totalCobrado / costoPorHora

                val horas: Int = totalHorasTrabajadas.toInt()
                val minutos: Int = ((totalHorasTrabajadas - horas) * 60).toInt()

                val bitmap = createBitmapWithText(clienteText, costoHoraText, totalCobradoText, horas, minutos)
                shareBitmapViaWhatsApp(bitmap)
            } else {
                // alerta indicando que algunos campos están vacíos
                mostrarAlerta("POR FAVOR, COMPLETA TODOS LOS CAMPOS")
            }
        } catch (e: Exception) {
            // alerta indicando el error ocurrido
            mostrarAlerta("Se produjo un error al procesar la información. Detalles: ${e.message}")
            e.printStackTrace()
        }
    }


    private fun mostrarAlerta(mensaje: String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
        builder.setMessage(mensaje)
            .setPositiveButton("ACEPTAR") { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun createBitmapWithText(cliente: String, costoHora: String, totalCobrado: String, horasTrabajadas: Int, minutos: Int): Bitmap {
        val width = 720
        val height = 1280

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.argb(255, 255, 255, 150))

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 40f
        }

        // Convertir el campo "CLIENTE" a mayúsculas
        val clienteEnMayusculas = cliente.toUpperCase()

        val calendar = Calendar.getInstance()
        val meses = arrayOf("Ene", "Feb", "Mar", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "Dic")

        val formato = SimpleDateFormat("EEEE", Locale("es", "ES"))
        val diaSemana = formato.format(calendar.time)

        val fechaHoy = "$diaSemana ${calendar.get(Calendar.DAY_OF_MONTH)}-${meses[calendar.get(Calendar.MONTH)]}-${calendar.get(Calendar.YEAR)}"

        val lines = listOf(
            "**********************  RECIBO  **********************",
            "","",
            " FECHA: $fechaHoy","",
            "-------------------------------------------------","",
            " CLIENTE: $clienteEnMayusculas","",
            "-------------------------------------------------","",
            " COSTO POR HORA/s: $$costoHora","",
            "-------------------------------------------------","",
            " CANT. HORA/s: $horasTrabajadas:$minutos","",
            "-------------------------------------------------","",
            " TOTAL COBRADO: $$totalCobrado"
        )

        // Dibujar en el canvas
        val lineHeight = paint.fontSpacing.toInt()
        val startY = height / 2 - (lines.size * lineHeight) / 2

        for ((index, line) in lines.withIndex()) {
            val y = startY + index * lineHeight
            canvas.drawText(line, 0f, y.toFloat(), paint)
        }
        return bitmap
    }

    private fun shareBitmapViaWhatsApp(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

        val file = File(cacheDir, "receipt.jpg")
        try {
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "image/jpeg"
        intent.setPackage("com.whatsapp")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }
}
