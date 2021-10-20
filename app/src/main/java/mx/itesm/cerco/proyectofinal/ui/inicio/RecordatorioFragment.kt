package mx.itesm.cerco.proyectofinal.ui.inicio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import mx.itesm.cerco.proyectofinal.databinding.FragmentInicioBinding
import mx.itesm.cerco.proyectofinal.ui.view.AdaptadorListaRecordatorio


class
RecordatorioFragment : Fragment() {

    private lateinit var inicioViewModel: RecordatorioVM
    private var _binding: FragmentInicioBinding? = null

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
    }

    private fun configurarEventos() {
        inicioViewModel.leerDatos() //Resultado demEvento como botón

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