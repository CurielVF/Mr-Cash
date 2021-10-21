package mx.itesm.cerco.proyectofinal.ui.tips

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import mx.itesm.cerco.proyectofinal.databinding.FragmentTipsBinding
import mx.itesm.cerco.proyectofinal.ui.tips.model.RenglonListener

class TipsFragment : Fragment(), RenglonListener {

    private lateinit var tipsViewModel: TipsViewModel
    private var _binding: FragmentTipsBinding? = null

    private val adaptadorListaTips = AdaptadorListaTips(arrayListOf())

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = TipsFragment()
    }

    private lateinit var viewModel: TipsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tipsViewModel = ViewModelProvider(this).get(TipsViewModel::class.java)

        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TipsViewModel::class.java)
        // TODO: Use the ViewModel

        configurarLista()
        registrarObservadores()
        registrarEventos()
    }

    private fun registrarEventos() {
        tipsViewModel.leerTips()
    }

    private fun registrarObservadores() {
        tipsViewModel.arrTips.observe(viewLifecycleOwner) {lista ->
            adaptadorListaTips.actualizarDatos(lista)

            //Quita el progress bar si lo pones
        }
    }

    private fun configurarLista() {
        binding.rvListaTips.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = adaptadorListaTips
        }

        adaptadorListaTips.listener = this
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Se hace click en el renglon #
    override fun clinckEnRenglon(posicion: Int) {
        //Se cambia pantalla a la de tip correspondiente
        val tipSeleccionado = adaptadorListaTips.arrTip[posicion]
        //val accion = Lista
        //findNavController().navigate(accion)
    }

}