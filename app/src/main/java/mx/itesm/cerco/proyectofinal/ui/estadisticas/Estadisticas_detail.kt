package mx.itesm.cerco.proyectofinal.ui.estadisticas

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.databinding.ActivityAgregarMetaBinding.inflate
import mx.itesm.cerco.proyectofinal.databinding.EstadisticasDetailFragmentBinding
import mx.itesm.cerco.proyectofinal.databinding.FragmentEstadisticasBinding

class Estadisticas_detail : Fragment() {



    private lateinit var viewModel: EstadisticasDetailViewModel
    private var _binding: EstadisticasDetailFragmentBinding? = null

    private val binding get() = _binding!!


    companion object {
        fun newInstance() = Estadisticas_detail()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(EstadisticasDetailViewModel::class.java)

        _binding = EstadisticasDetailFragmentBinding.inflate(inflater, container , false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EstadisticasDetailViewModel::class.java)
        // TODO: Use the ViewModel

        configuradorEventos()

    }

    private fun configuradorEventos() {
        binding.btnAtras.setOnClickListener {
            requireActivity().onBackPressed()

        }
    }

}