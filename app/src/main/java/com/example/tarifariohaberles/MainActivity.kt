package com.example.tarifariohaberles

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var horasIngresoMain: EditText
    private lateinit var minutosIngresoMain: EditText
    private lateinit var horasEgresoMain: EditText
    private lateinit var minutosEgresoMain: EditText
    private lateinit var costoHoraMain: EditText
    private lateinit var totalCobrarMain: TextView
    private lateinit var totalChoferMain: TextView

    private var validacionExitosa: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //recuperar los datos ingresados por el usuario
        horasIngresoMain = findViewById(R.id.horasIngreso)
        minutosIngresoMain = findViewById(R.id.minutosIngreso)
        horasEgresoMain = findViewById(R.id.horasEgreso)
        minutosEgresoMain = findViewById(R.id.minutosEgreso)
        costoHoraMain = findViewById(R.id.costoHora)
        totalCobrarMain = findViewById(R.id.totalCobrar)
        totalChoferMain = findViewById(R.id.totalChofer)

        //NUEVA VISTA
        otra()
    }

    //
    fun otra(){
        val btnOpenSecondActivity = findViewById<ImageView>(R.id.btnOpenSecondActivity)
        btnOpenSecondActivity.setOnClickListener {
          val intent = Intent(this, SecondActivity::class.java)
          startActivity(intent) //intencion de ir a la nueva ventana
        }
    }

    //funcion encargada de realizar todos los calculos necesarios correspondiente a la funcionalidad de la app
    @SuppressLint("SetTextI18n")
    fun Cacular(view: View) {
        var costeHoras: Double = 0.0
        var costeMinutos: Double = 0.0

        try {
            val horaInicio = horasIngresoMain.text.toString().toDouble()
            val horaFin = horasEgresoMain.text.toString().toDouble()

            if (horaInicio > horaFin) {
                totalCobrarMain.text = "La hora de inicio no puede ser mayor que la hora de fin"
                totalChoferMain.text = ""
                // Cambiar el color del texto a rojo
                totalCobrarMain.setTextColor(Color.RED)
                totalChoferMain.setTextColor(Color.RED)
                return  // Salir de la función si la validación falla
            }

            if (horasIngresoMain.text.toString().toDouble() != null && horasEgresoMain.text.toString().toDouble() != null) {
                costeHoras =
                    (-(horasIngresoMain.text.toString().toDouble() - (horasEgresoMain.text.toString().toDouble()))) * costoHoraMain.text.toString().toDouble()
            }
            if (minutosIngresoMain.text.toString().toDouble() != null && minutosEgresoMain.text.toString().toDouble() != null) {
                costeMinutos =
                    (-(minutosIngresoMain.text.toString().toDouble() - minutosEgresoMain.text.toString().toDouble())) * (costoHoraMain.text.toString().toDouble() / 60)
            }
            val sumaTotal: Double = costeHoras + costeMinutos
            val sumaChofer: Double = ((costeHoras + costeMinutos) * 0.15)
            val totalCobro: String = "Total a cobrar: $ ${String.format("%.2f", sumaTotal)}"
            val totalChofer: String = "Ganancia chofer: $ ${String.format("%.2f", sumaChofer)}"
            totalCobrarMain.text = totalCobro
            totalChoferMain.text = totalChofer

            // Restaurar el color original del texto si la validación es exitosa
            totalCobrarMain.setTextColor(Color.WHITE)
            totalChoferMain.setTextColor(Color.WHITE)

            validacionExitosa = true
        } catch (e: Exception) {
            validacionExitosa = false
            totalCobrarMain.text = "COMPLETAR TODOS LOS CAMPOS"
            totalChoferMain.text = "SI NO TIENE VALOR PONER 0"
            // Cambiar el color del texto a rojo
            totalCobrarMain.setTextColor(Color.RED)
            totalChoferMain.setTextColor(Color.RED)
        }
    }

    //funcion que realiza el reset de los campos
    @SuppressLint("SetTextI18n")
    fun ReiniciarApp(view: View):Unit{
        horasIngresoMain.text.clear()
        minutosIngresoMain.text.clear()
        horasEgresoMain.text.clear()
        minutosEgresoMain.text.clear()
        costoHoraMain.text.clear()
        totalCobrarMain.text = "TOTAL A COBRAR"
        totalChoferMain.text = "TOTAL PARA CHOFER (%15)"
        totalCobrarMain.setTextColor(Color.WHITE)
        totalChoferMain.setTextColor(Color.WHITE)
    }
}