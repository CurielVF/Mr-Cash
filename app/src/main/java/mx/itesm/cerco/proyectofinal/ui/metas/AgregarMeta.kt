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
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.databinding.ActivityAgregarMetaBinding
import mx.itesm.cerco.proyectofinal.ui.model.Meta
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import java.time.LocalDate
import java.util.*
import android.widget.CalendarView

import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import mx.itesm.cerco.proyectofinal.Login


class AgregarMeta : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarMetaBinding
    private lateinit var baseDatos: FirebaseDatabase
    @RequiresApi(Build.VERSION_CODES.O)
    private var fecha: LocalDate=LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarMetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        baseDatos = Firebase.database

        configurarEventos()
        val calendar = Calendar.getInstance()
        binding.cvFechaMeta.minDate = calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarEventos() {
        binding.btnAgregarMeta.setOnClickListener {
            agregarMeta()
        }

        binding.cvFechaMeta.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            fecha = LocalDate.of(year,month,dayOfMonth)
        })

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
            val tipo = binding.etTipoMeta.text.toString()
            val fechaLimite=fecha.toString()
            // Crea un objeto alumno con los datos capturados
            println(fechaLimite)
            val meta = Meta(nombre,fechaLimite,monto,tipo)

            myRef.setValue(meta)
            val intLogin =Intent(this,MetasFragment::class.java)
            startActivity(intLogin)
        } catch (e: Exception){

            Toast.makeText(baseContext,"Debes introducir todos los campos", Toast.LENGTH_SHORT).show()
        }

    }
}