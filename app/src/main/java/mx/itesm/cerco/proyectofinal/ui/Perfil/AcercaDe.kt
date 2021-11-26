package mx.itesm.cerco.proyectofinal.ui.Perfil

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.databinding.AcercaDeFragmentBinding
import mx.itesm.cerco.proyectofinal.databinding.FragmentPerfilBinding
import android.content.pm.PackageManager

import android.content.pm.PackageInfo




class AcercaDe : Fragment() {

    companion object {
        fun newInstance() = AcercaDe()
    }
    private var _binding: AcercaDeFragmentBinding? = null
    private lateinit var acercadeViewModel: AcercaDeViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        acercadeViewModel =
            ViewModelProvider(this).get(AcercaDeViewModel::class.java)
        _binding = AcercaDeFragmentBinding.inflate(inflater, container, false)
        configurarObservadores()
        val root: View = binding.root
        return root

    }

    private fun configurarObservadores() {
        try {
            val pInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            val version = pInfo.versionName
            binding.tvVersion.text =version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarObservadores()


    }

}