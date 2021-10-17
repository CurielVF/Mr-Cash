package mx.itesm.cerco.proyectofinal.ui.metas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.databinding.ActivityAgregarMetaBinding
import mx.itesm.cerco.proyectofinal.ui.model.Meta

class AgregarMeta : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarMetaBinding
    private lateinit var baseDatos: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarMetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        baseDatos = Firebase.database

        configurarEventos()
    }

    private fun configurarEventos() {
        binding.btnAgregarMeta.setOnClickListener {
            agregarMeta()
        }
    }

    private fun agregarMeta() {
        val database = FirebaseDatabase.getInstance()
        val nombre = binding.etNombreMeta.text.toString()
        val fecha = binding.etFechaMeta.text.toString()
        val monto = binding.etMontoMeta.text.toString().toDouble()
        val tipo = binding.etTipoMeta.text.toString()

        val key = database.getReference("Metas").push().getKey()
        val myRef =database.getReference("Metas/"+key)

        // Crea un objeto alumno con los datos capturados
        val meta = Meta(nombre,fecha,monto,tipo)

        myRef.setValue(meta)
    }
}