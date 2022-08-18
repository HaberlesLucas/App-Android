package com.example.haberles20

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var horasIngresoMain: EditText
    private lateinit var minutosIngresoMain: EditText
    private lateinit var horasEgresoMain: EditText
    private lateinit var minutosEgresoMain: EditText
    private lateinit var costoHoraMain: EditText
    private lateinit var totalCobrarMain: TextView
    private lateinit var totalChoferMain: TextView
    private lateinit var botonMain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //insertar icono parte superior de la App
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)

        //recuperar los datos ingresados por el usuario
        horasIngresoMain = findViewById<EditText>(R.id.horasIngreso)
        minutosIngresoMain = findViewById<EditText>(R.id.minutosIngreso)
        horasEgresoMain = findViewById<EditText>(R.id.horasEgreso)
        minutosEgresoMain = findViewById<EditText>(R.id.minutosEgreso)
        costoHoraMain = findViewById<EditText>(R.id.costoHora)
        totalCobrarMain = findViewById<TextView>(R.id.totalCobrar)
        totalChoferMain = findViewById<TextView>(R.id.totalChofer)
        botonMain = findViewById<Button>(R.id.boton)
    }
    //funcion encargada de realizar todos los calculos
    //necesarios correspondiente a la funcionalidad de la app
    fun Cacular(view: View):Unit{
        var costeHoras:Double = 0.0
        var costeMinutos:Double = 0.0

        try {
            if (horasIngresoMain.text.toString().toDouble() != null && horasEgresoMain.text.toString().toDouble() != null) {
                //costeHoras = horasIngresoMain.text.toString().toDouble() * costoHoraMain.text.toString().toDouble()
                costeHoras = (-(horasIngresoMain.text.toString().toDouble() - (horasEgresoMain.text.toString().toDouble()))) * costoHoraMain.text.toString().toDouble()
            }
            if (minutosIngresoMain.text.toString().toDouble() != null && minutosEgresoMain.text.toString().toDouble() != null) {
                //costeMinutos = minutosIngresoMain.text.toString().toDouble() * (costoHoraMain.text.toString().toDouble() / 60)
                costeMinutos = (-(minutosIngresoMain.text.toString().toDouble() - minutosEgresoMain.text.toString().toDouble())) * (costoHoraMain.text.toString().toDouble() / 60)
            }
            val sumaTotal: Double = costeHoras + costeMinutos
            val sumaChofer: Double = ((costeHoras + costeMinutos)/10)
            val totalCobro: String =  "Total a cobrar: $ $sumaTotal"
            val totalChofer: String = "Ganancia chofer: $ $sumaChofer"
            totalCobrarMain.text = totalCobro
            totalChoferMain.text = totalChofer

        }catch (e:Exception){
            totalCobrarMain.text = "COMPLETAR TODOS LOS CAMPOS"
            totalChoferMain.text = "SI NO TIENE VALOR PONER '0'"
        }
    }
    //funcion que realiza el reset de los campos
    fun ReiniciarApp(view: View):Unit{
        horasIngresoMain.text.clear()
        minutosIngresoMain.text.clear()
        horasEgresoMain.text.clear()
        minutosEgresoMain.text.clear()
        costoHoraMain.text.clear()
        totalCobrarMain.text = "TOTAL A COBRAR"
        totalChoferMain.text = "TOTAL PARA CHOFER (%10)"
    }
}