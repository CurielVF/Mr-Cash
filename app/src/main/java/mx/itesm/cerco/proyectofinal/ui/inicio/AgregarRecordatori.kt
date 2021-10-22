package mx.itesm.cerco.proyectofinal.ui.inicio

import android.R
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mx.itesm.cerco.proyectofinal.databinding.ActivityAgregarRecordatoriBinding
import mx.itesm.cerco.proyectofinal.ui.estadisticas.TipoRecordatorios
import mx.itesm.cerco.proyectofinal.ui.metas.TiposMetas
import mx.itesm.cerco.proyectofinal.ui.model.Recordatorio
import java.lang.Double
import java.time.LocalDate
import java.util.*

class AgregarRecordatori : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarRecordatoriBinding
    private lateinit var baseDatos: FirebaseDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    private var fecha: LocalDate = LocalDate.now()
    lateinit var opcionTipo: Spinner
    lateinit var tipoRecordatorio: String
    var opcionesRecordatorio = arrayOf(
        TipoRecordatorios.Agua,
        TipoRecordatorios.Colegiatura,
        TipoRecordatorios.Entretenimiento,
        TipoRecordatorios.Gimnasio,
        TipoRecordatorios.Hogar,
        TipoRecordatorios.Tarjetas,
        TipoRecordatorios.Telefonia,
        TipoRecordatorios.Luz,
        TipoRecordatorios.Otro
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarRecordatoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        baseDatos = Firebase.database

        configurarObservadores()
        configurarEventos()
    }

    private fun configurarObservadores() {
        opcionTipo = binding.sORecordatorioTipo
        opcionTipo.adapter = ArrayAdapter<TipoRecordatorios>(this, R.layout.simple_list_item_1, opcionesRecordatorio)
        val calendar = Calendar.getInstance()
        binding.cvFechaR.minDate = calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarEventos() {
        binding.cvFechaR.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            fecha = LocalDate.of(year, month + 1, dayOfMonth)
        })

        binding.btnAgregarRecordatorio.setOnClickListener({
            agregarRecordatorio()
        })

        opcionTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tipoRecordatorio = opcionesRecordatorio.get(p2).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("Selecciona una opción")

            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun agregarRecordatorio() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance()

        val key = database.getReference(uid+"/Recordatorios").push().getKey()
        val myRef =database.getReference(uid+"/Recordatorios/"+key)

        try{
            val nombre = binding.etNombreR.text.toString()
            val monto = binding.etMontoR.text.toString().toDouble()
            val fecha = fecha.toString()
            val tipo = tipoRecordatorio
            val hora = binding.etHora.text.toString()

            val recordatorio = Recordatorio(nombre,fecha,monto,tipo,hora)
            myRef.setValue(recordatorio)
            super.onBackPressed();
        } catch (e: Exception){
            try {
                Double.parseDouble(binding.etMontoR.text.toString())
            }
            catch (e: NumberFormatException) {
                binding.etMontoR.setError("Monto inválido")
            }
            if (binding.etNombreR.text.toString().isBlank()){
                binding.etNombreR.setError("Nombre inválido")
            }
            Toast.makeText(baseContext,"Debes introducir todos los campos", Toast.LENGTH_SHORT).show()
        }
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