package mx.itesm.cerco.proyectofinal.ui.metas

import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
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
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import mx.itesm.cerco.proyectofinal.ui.Constantes
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import android.graphics.drawable.Drawable




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
    private var estadoMeta: String = "En progreso"
    private var colorEstado: String = Constantes.color_progreso

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalleMetaBinding.inflate(layoutInflater)
        return  binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        montoReal = args.meta.montoReal!!
        binding.tvNombreMetaDetalle.text=args.meta.nombre.toString()
        binding.tvFechaMetaDetalle.text=args.meta.fechaLimite.toString()
        binding.tvMontoMetaDetalle.text=args.meta.precio.toString()
        binding.tvMontoRealMetaDetalle.text=montoReal.toString()
        obtenerEstadoMeta()

        binding.pbMeta.progress= (montoReal/args.meta.precio!!*100).toInt()
        binding.pbMeta.scaleY = 5f

      when (args.meta.tipo) {
            "ENTRETENIMIENTO" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_entretenimiento)
            "HOGAR" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_hogar)
            "COMIDA" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_comida)
            "OTRO" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_otro)
            "PERSONAL" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_personal)
            "VEHICULO" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_vehiculo)
            "VIAJES" -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_viaje)
            else -> binding.ivTipoDetalleMeta.setImageResource(R.drawable.ic_tipo_otro)
        }

        configurarEventos()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            binding.pbMeta.progress= (montoReal/args.meta.precio!!*100).toInt()
            obtenerEstadoMeta()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerEstadoMeta(){
        if(montoReal >= args.meta.precio!!){
            estadoMeta = "Completado"
            colorEstado = Constantes.color_completado
            montoReal = args.meta.precio!!
            binding.etAgregarMontoMeta.isEnabled = false
            binding.btnAgregarMontoMeta.isEnabled = false
            binding.btnMonto10Meta.isEnabled = false
            binding.btnMonto50Meta.isEnabled = false
            binding.btnMonto100Meta.isEnabled = false
            binding.btnMonto500Meta.isEnabled = false
            binding.btnMonto1000Meta.isEnabled = false
            binding.btnMonto10000Meta.isEnabled = false

        }
        else{

            val diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(args.meta.fechaLimite))
            if (diasRestantes < 0){
                estadoMeta = "Con retraso"
                colorEstado = Constantes.color_retraso
            }

        }
        binding.ivTipoDetalleMeta.setColorFilter(Color.parseColor(colorEstado))
        binding.tvEstadoMetaDetalle.text = estadoMeta
        binding.tvEstadoMetaDetalle.setTextColor(Color.parseColor(colorEstado))
        binding.pbMeta.progressDrawable.setColorFilter(Color.parseColor(colorEstado), PorterDuff.Mode.SRC_IN)
    }
}