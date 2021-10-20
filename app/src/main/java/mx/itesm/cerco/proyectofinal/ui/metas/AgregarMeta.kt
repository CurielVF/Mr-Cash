package mx.itesm.cerco.proyectofinal.ui.metas

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mx.itesm.cerco.proyectofinal.databinding.ActivityAgregarMetaBinding
import mx.itesm.cerco.proyectofinal.ui.model.Meta
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import java.time.LocalDate
import java.util.*

import android.widget.CalendarView.OnDateChangeListener
import mx.itesm.cerco.proyectofinal.Login
import java.time.Period
import android.R
import android.view.View
import android.widget.*
import java.lang.Double


class AgregarMeta : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarMetaBinding
    private lateinit var baseDatos: FirebaseDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    private var fecha: LocalDate=LocalDate.now()
    lateinit var opcionTipo: Spinner
    lateinit var resultadoTipo: String
    var opcionesTipos = arrayOf(
        TiposMetas.ENTRETENIMIENTO,
        TiposMetas.HOGAR,
        TiposMetas.COMIDA,
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

    private fun configurarObservadores() {
        opcionTipo = binding.spOpcionTipo
        opcionTipo.adapter = ArrayAdapter<TiposMetas>(this, R.layout.simple_list_item_1, opcionesTipos)
        val calendar = Calendar.getInstance()
        binding.cvFechaMeta.minDate = calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarEventos() {
        binding.btnAgregarMeta.setOnClickListener {
            agregarMeta()
        }

        binding.cvFechaMeta.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            fecha = LocalDate.of(year,month + 1,dayOfMonth)
        })

        opcionTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                resultadoTipo = opcionesTipos.get(p2).toString()
                when (resultadoTipo) {
                    "ENTRETENIMIENTO" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_entretenimiento)
                    "HOGAR" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_hogar)
                    "COMIDA" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_comida)
                    "OTRO" -> binding.ivAgregarTipoMeta.setImageResource(mx.itesm.cerco.proyectofinal.R.drawable.ic_tipo_otro)
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
            val nombre = binding.etNombreMeta.text.toString()
            val monto = binding.etMontoMeta.text.toString().toDouble()
            val tipo = resultadoTipo
            val fechaLimite=fecha.toString()
            val fechaCreacion=LocalDate.now().toString()
            // Crea un objeto alumno con los datos capturados
            val meta = Meta(nombre,fechaLimite,monto,tipo,null,null,fechaCreacion)

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