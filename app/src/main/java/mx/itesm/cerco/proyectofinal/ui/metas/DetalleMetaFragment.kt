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
    private var nuevoMonto: Double? = 0.0

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
        binding.tvFechaMetaDetalle.text=args.meta.fechaLimite.toString()
        binding.tvMontoMetaDetalle.text=args.meta.precio.toString()
        binding.tvMontoRealMetaDetalle.text=args.meta.precio.toString()
        binding.etAgregarMontoMeta.setText(nuevoMonto.toString())
        when (args.meta.tipo) {
            "ENTRETENIMIENTO" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_entretenimiento)
            "HOGAR" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_hogar)
            "COMIDA" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_comida)
            "OTRO" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_otro)
            else -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_otro)
        }

        configurarEventos()
    }

    private fun configurarEventos() {
        binding.btnMonto10Meta.setOnClickListener {
            sumarSaldo(10.0)
        }
        binding.btnMonto50Meta.setOnClickListener {
            sumarSaldo(50.0)
        }
        binding.btnMonto100Meta.setOnClickListener {
            sumarSaldo(100.0)
        }
        binding.btnMonto500Meta.setOnClickListener {
            sumarSaldo(500.0)
        }
        binding.btnMonto1000Meta.setOnClickListener {
            sumarSaldo(1000.0)
        }
        binding.btnMonto10000Meta.setOnClickListener {
            sumarSaldo(10000.0)
        }
        binding.btnAgregarMontoMeta.setOnClickListener {
            binding.etAgregarMontoMeta.setText("0.0")
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetalleMetaFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun sumarSaldo(saldo : Double){
        nuevoMonto = binding.etAgregarMontoMeta.text.toString().toDouble()
        nuevoMonto = nuevoMonto?.plus(saldo)
        binding.etAgregarMontoMeta.setText(nuevoMonto.toString())
    }

}