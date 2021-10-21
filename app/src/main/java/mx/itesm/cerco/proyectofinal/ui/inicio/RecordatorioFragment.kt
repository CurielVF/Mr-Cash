package mx.itesm.cerco.proyectofinal.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import mx.itesm.cerco.proyectofinal.Login
import mx.itesm.cerco.proyectofinal.MainActivity
import mx.itesm.cerco.proyectofinal.databinding.FragmentInicioBinding
import mx.itesm.cerco.proyectofinal.ui.view.AdaptadorListaRecordatorio

class
RecordatorioFragment : Fragment() {

    private lateinit var inicioViewModel: RecordatorioVM
    private var _binding: FragmentInicioBinding? = null

    private val mAuth = FirebaseAuth.getInstance()

    //Adaptador para el RecycleView
    private val adaptadorListaRecordatorio =AdaptadorListaRecordatorio(arrayListOf())


    // This property is only valid between onCreateView and
    // onDestroyView.
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
        inicioViewModel.leerDatos() //Resultado demEvento como botón



        binding.btnSignout.setOnClickListener{
            mAuth.signOut()
            AuthUI.getInstance().signOut(requireContext())
            val intLogin = Intent(context, Login::class.java)
            startActivity(intLogin)
        }
    }

    private fun configurarObservadores() {

        inicioViewModel.arrRecoratorios.observe(viewLifecycleOwner){lista ->
            adaptadorListaRecordatorio.actualizar(lista)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}