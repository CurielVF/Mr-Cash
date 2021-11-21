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
import android.text.InputFilter;
import android.text.InputType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetalleMetaFragment : Fragment() {

    private lateinit var baseDatos: FirebaseDatabase

    companion object {
        fun newInstance() = DetalleMetaFragment()
    }

    private lateinit var viewModel: DetalleMetaFragmentViewModel
    private lateinit var binding : FragmentDetalleMetaBinding
    private val args : DetalleMetaFragmentArgs by navArgs<DetalleMetaFragmentArgs>()
    private var nuevoMonto: Double? = 0.0
    private var montoReal: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalleMetaBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        montoReal = args.meta.montoReal!!
        binding.tvNombreMetaDetalle.text=args.meta.nombre.toString()
        binding.tvFechaMetaDetalle.text=args.meta.fechaLimite.toString()
        binding.tvMontoMetaDetalle.text=args.meta.precio.toString()
        binding.tvMontoRealMetaDetalle.text=montoReal.toString()

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
            if (!binding.etAgregarMontoMeta.text.toString().isBlank() && binding.etAgregarMontoMeta.text.toString() != "."){
                nuevoMonto = String.format("%.2f",binding.etAgregarMontoMeta.text.toString().toDouble()).toDouble()
            }
            else{
                nuevoMonto = 0.0
            }
            montoReal = montoReal!! + nuevoMonto!!
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val database = FirebaseDatabase.getInstance()
            val myRef =database.getReference(uid+"/Metas/"+args.meta.llaveMeta+"/montoReal")
            myRef.setValue(montoReal)
            binding.tvMontoRealMetaDetalle.text=montoReal.toString()
            binding.etAgregarMontoMeta.setText("")
            nuevoMonto = 0.0
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetalleMetaFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun sumarSaldo(saldo : Double){
        if (!binding.etAgregarMontoMeta.text.toString().isBlank() && binding.etAgregarMontoMeta.text.toString() != "."){
            nuevoMonto = String.format("%.2f",binding.etAgregarMontoMeta.text.toString().toDouble()).toDouble()
        }
        else{
            nuevoMonto = 0.0
        }

        nuevoMonto = nuevoMonto?.plus(saldo)
        binding.etAgregarMontoMeta.setText(nuevoMonto.toString())
    }

}