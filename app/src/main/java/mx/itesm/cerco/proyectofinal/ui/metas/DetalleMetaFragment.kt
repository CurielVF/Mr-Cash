package mx.itesm.cerco.proyectofinal.ui.metas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.databinding.FragmentDetalleMetaBinding

class DetalleMetaFragment : Fragment() {

    companion object {
        fun newInstance() = DetalleMetaFragment()
    }

    private lateinit var viewModel: DetalleMetaFragmentViewModel
    private lateinit var binding : FragmentDetalleMetaBinding
    private val args : DetalleMetaFragmentArgs by navArgs<DetalleMetaFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalleMetaBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNombreMetaDetalle.text=args.meta.nombre.toString()

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetalleMetaFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}