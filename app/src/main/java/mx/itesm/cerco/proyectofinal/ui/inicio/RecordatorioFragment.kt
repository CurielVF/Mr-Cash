package mx.itesm.cerco.proyectofinal.ui.inicio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import mx.itesm.cerco.proyectofinal.databinding.FragmentInicioBinding
import mx.itesm.cerco.proyectofinal.ui.model.Recordatorio
import mx.itesm.cerco.proyectofinal.ui.view.AdaptadorListaRecordatorio
import java.text.SimpleDateFormat
import java.util.*

class
RecordatorioFragment : Fragment() {

    private lateinit var inicioViewModel: RecordatorioVM

    private var _binding: FragmentInicioBinding? = null
    private var recordatorios: MutableList<Recordatorio> = mutableListOf<Recordatorio>()

    //Adaptador para el RecycleView
    private val adaptadorListaRecordatorio = AdaptadorListaRecordatorio(arrayListOf())

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inicioViewModel =
            ViewModelProvider(this).get(RecordatorioVM::class.java)

        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarObservadores()
        configurarEventos()
        configurarRecycleView()
    }

    private fun configurarRecycleView() {
        //Llama al método y regresa THIS, regresa el objeto
        binding.rvListaRecordatorios.apply {
            layoutManager =LinearLayoutManager(context)
            adapter= adaptadorListaRecordatorio
        }
        binding.fabAgregarRecordatorio.setOnClickListener(){
            val intAgregarRecordatorio = Intent(getActivity(),AgregarRecordatori::class.java)
            startActivity(intAgregarRecordatorio)
        }
    }

    private fun configurarEventos() {
        leerRecordatorios()
    }

    private fun configurarObservadores() {
        inicioViewModel.arrRecordatorios.observe(viewLifecycleOwner){ lista->
            adaptadorListaRecordatorio.actualizar(lista)
            binding.progressBar2.visibility = View.GONE
            if(lista.isEmpty()){
                binding.rvListaRecordatorios.visibility = View.GONE
                binding.emptyView2.visibility = View.VISIBLE
            }else{
                binding.rvListaRecordatorios.visibility = View.VISIBLE
                binding.emptyView2.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun leerRecordatorios(): MutableList<Recordatorio>{
        val database = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val myRef =database.getReference(uid+"/Recordatorios")

        myRef.addValueEventListener(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                recordatorios.clear()
                for(registro in snapshot.children) {
                    val nombre = registro.child("nombrePago").getValue(String::class.java)
                    val fechaLimite = registro.child("fechaPago").getValue(String::class.java)
                    val precio = registro.child("cantidadPago").getValue(Double::class.java)
                    val tipo = registro.child("tipo").getValue(String::class.java)
                    val hora = registro.child("hora").getValue(String::class.java)
                    val uuid = registro.child("uuidRecordatorio").getValue(String::class.java)
                    val frecuencia = registro.child("frecuencia").getValue(String::class.java)
                    val id = registro.key
                    var delay:Long = (0.0).toLong()
                    try {
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:ss")
                        val date = sdf.parse(fechaLimite+" "+hora)
                        val cal = Calendar.getInstance()
                        cal.time = date

                        val customTime = cal.timeInMillis
                        val currentTime = System.currentTimeMillis()
                        delay = customTime - currentTime
                    }
                    catch (e:Exception){

                    }

                    print("delay:" + delay)
                    if (delay >= 0){
                   }
                    recordatorios.add(Recordatorio(nombre,fechaLimite,precio,tipo,hora,id,uuid,frecuencia))

                }
                val recordatoriosOrdenados = recordatorios
                    .sortedWith( compareBy({ it.fechaPago }, { it.hora}) )
                inicioViewModel.setRecordatorios(recordatoriosOrdenados)
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        return recordatorios
    }
}