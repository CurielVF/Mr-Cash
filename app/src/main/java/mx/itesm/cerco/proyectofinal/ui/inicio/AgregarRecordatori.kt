package mx.itesm.cerco.proyectofinal.ui.inicio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mx.itesm.cerco.proyectofinal.databinding.ActivityAgregarRecordatoriBinding

class AgregarRecordatori : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarRecordatoriBinding
    private lateinit var baseDatos: FirebaseDatabase




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarRecordatoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        baseDatos = Firebase.database

        //configurarObservadores()
        configurarEventos()



    }

    private fun configurarEventos() {
       binding.etFecha.setOnClickListener(){
           showDatPickerDialog()
       }
    }

    private fun showDatPickerDialog() {
        val datePicker =DatePickerFragment{dia,mes,año ->onDateSelected(dia,mes,año)}
        datePicker.show(supportFragmentManager,"datePicker")
    }
    //Muestra la día seleccionada en el EditText
    fun onDateSelected(dia: Int, mes: Int, año: Int){
        binding.etFecha?.setText("$dia de $mes del $año")

    }

    fun SeleccionarHora(view: View) {
        val hora = TimePicker{hora,dia -> mostrarHora(hora,dia)}
        hora.show(supportFragmentManager,"TimePicker")
    }
    //Muestra la hora seleccionada en el EditText
    fun mostrarHora(hora: Int,minuto:Int){
        binding.etHora?.setText("$hora : $minuto")

    }



}