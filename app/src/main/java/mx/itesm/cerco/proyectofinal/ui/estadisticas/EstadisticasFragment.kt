package mx.itesm.cerco.proyectofinal.ui.estadisticas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import mx.itesm.cerco.proyectofinal.databinding.FragmentEstadisticasBinding

class EstadisticasFragment : Fragment() {

    private lateinit var estadisticasViewModel: EstadisticasViewModel
    private var _binding: FragmentEstadisticasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        estadisticasViewModel =
            ViewModelProvider(this).get(EstadisticasViewModel::class.java)

        _binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        estadisticasViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}