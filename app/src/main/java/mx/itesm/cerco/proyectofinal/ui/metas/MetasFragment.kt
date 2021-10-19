package mx.itesm.cerco.proyectofinal.ui.metas

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
import mx.itesm.cerco.proyectofinal.databinding.FragmentMetasBinding
import mx.itesm.cerco.proyectofinal.ui.model.Meta
import mx.itesm.cerco.proyectofinal.ui.view.AdaptadorListaMetas
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

class MetasFragment : Fragment() {

    private lateinit var metasViewModel: MetasViewModel
    private var _binding: FragmentMetasBinding? = null
    private var metas : MutableList<Meta> = mutableListOf<Meta>()

    //Adaptador para el RecycleView
    private val adaptadorListaMeta = AdaptadorListaMetas(arrayListOf())


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        metasViewModel =
            ViewModelProvider(this).get(MetasViewModel::class.java)

        _binding = FragmentMetasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarObservadores()
        configurarEventos()
        configurarRecycleView()
        configurarRecycleView()
    }

    private fun configurarRecycleView() {
        //Llama al método y regresa THIS, regresa el objeto
        binding.rvListaMetas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter= adaptadorListaMeta
        }

        binding.fabAgregarMeta.setOnClickListener {
            val intAgregarMeta =Intent(getActivity(),AgregarMeta::class.java)
            startActivity(intAgregarMeta)
        }
    }

    private fun configurarEventos() {
        leerMetas()
    }

    private fun configurarObservadores() {

        metasViewModel.arrMetas.observe(viewLifecycleOwner){lista ->
            adaptadorListaMeta.actualizar(lista)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    fun leerMetas(): MutableList<Meta>{
        val database = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val myRef =database.getReference(uid+"/Metas")

        myRef.addValueEventListener(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                metas.clear()
                for(registro in snapshot.children) {
                    val nombre = registro.child("nombre").getValue(String::class.java)
                    val fechaLimite = registro.child("fechaLimite").getValue(String::class.java)
                    val precio = registro.child("precio").getValue(Double::class.java)
                    val tipo = registro.child("tipo").getValue(String::class.java)
                    val periodo = Period.between(LocalDate.now(),LocalDate.parse(fechaLimite))

                    val diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.parse(fechaLimite))
                    val ahorroNecesario = precio?.div(diasRestantes)
                    metas.add(Meta(nombre,fechaLimite,precio,tipo,periodo,ahorroNecesario))
                }

                metasViewModel.setMetas(metas)
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        return metas
    }
}