package mx.itesm.cerco.proyectofinal.ui.metas

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mx.itesm.cerco.proyectofinal.databinding.ActivityAgregarMetaBinding
import mx.itesm.cerco.proyectofinal.ui.model.Meta
import com.google.firebase.auth.FirebaseAuth

import java.time.LocalDate
import java.util.*

import android.widget.CalendarView.OnDateChangeListener
import android.R
import android.view.View
import android.widget.*
import mx.itesm.cerco.proyectofinal.DatePickerFragment
import java.io.IOException
import java.lang.Double
import java.text.SimpleDateFormat


class AgregarMeta : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarMetaBinding
    private lateinit var baseDatos: FirebaseDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    private var fecha: LocalDate? =null
    lateinit var opcionTipo: Spinner
    lateinit var resultadoTipo: String
    var opcionesTipos = arrayOf(
        TiposMetas.COMIDA,
        TiposMetas.EDUCACION,
        TiposMetas.ENTRETENIMIENTO,
        TiposMetas.HOGAR,
        TiposMetas.PERSONAL,
        TiposMetas.SALUD,
        TiposMetas.VEHICULO,
        TiposMetas.VIAJES,
        TiposMetas.OTRO
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarMetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        baseDatos = Firebase.database

        configurarObservadores()
        configurarEventos()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarObservadores() {
        opcionTipo = binding.spOpcionTipo
        opcionTipo.adapter = ArrayAdapter<TiposMetas>(this, R.layout.simple_list_item_1, opcionesTipos)
        binding.dpFechaMeta.minDate = System.currentTimeMillis() - 1000;
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarEventos() {
        binding.btnAgregarMeta.setOnClickListener {
            agregarMeta()
        }

        opcionTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                resultadoTipo = opcionesTipos.get(p2).toString()
                when (resultadoTipo) {
                    "ENTRETENIMIENTO" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_entretenimiento)
                    "HOGAR" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_hogar)
                    "COMIDA" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_comida)
                    "OTRO" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_otro)
                    "PERSONAL" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_personal)
                    "VEHICULO" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_vehiculo)
                    "VIAJES" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_viaje)
                    "SALUD" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_salud)
                    "EDUCACION" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_educacion)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("Selecciona una opción")

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun agregarMeta() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance()

        val key = database.getReference(uid+"/Metas").push().getKey()
        val myRef =database.getReference(uid+"/Metas/"+key)

        try {
            val fechaCalendario = Calendar.getInstance()
            fechaCalendario.set(
                binding.dpFechaMeta.year, binding.dpFechaMeta.month, binding.dpFechaMeta.dayOfMonth
            )
            val format = SimpleDateFormat("yyyy-MM-dd")
            val fechaLimite = format.format(fechaCalendario.time)

            val nombre = binding.etNombreMeta.text.toString()
            val monto = binding.etMontoMeta.text.toString().toDouble()
            val tipo = resultadoTipo
            val fechaCreacion=LocalDate.now().toString()
            // Crea un objeto alumno con los datos capturados
            val meta = Meta(nombre,fechaLimite,monto,tipo,null,null,fechaCreacion)
            if (fechaLimite==null || nombre.isBlank() || binding.etMontoMeta.text.toString().isBlank()){
                throw IOException()
            }

            myRef.setValue(meta)
            super.onBackPressed();
        } catch (e: Exception){
            try {
                Double.parseDouble(binding.etMontoMeta.text.toString())
            }
            catch (e: NumberFormatException) {
                binding.etMontoMeta.setError("Monto inválido")
            }
            if (binding.etNombreMeta.text.toString().isBlank()){
                binding.etNombreMeta.setError("Nombre inválido")
            }
            Toast.makeText(baseContext,"Debes introducir todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}