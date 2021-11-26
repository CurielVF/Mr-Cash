package mx.itesm.cerco.proyectofinal.ui.estadisticas

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.anychart.AnyChart
import com.anychart.AnyChartView
import mx.itesm.cerco.proyectofinal.databinding.FragmentEstadisticasBinding
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.ui.metas.TiposMetas
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import mx.itesm.cerco.proyectofinal.ui.model.Meta
import java.lang.Exception
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit


class EstadisticasFragment : Fragment() {
    lateinit var pie: Pie
    private lateinit var estadisticasViewModel: EstadisticasViewModel
    private var _binding: FragmentEstadisticasBinding? = null
    lateinit var opcionEstadistica: Spinner
    lateinit var resultadoEstadistica: String
    private var opcionesEstadisticas = arrayOf(
        "TIPOS DE METAS",
        "TIPOS DE RECORDATORIOS"
    )
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var tiposMetas : MutableList<ValueDataEntry> = mutableListOf<ValueDataEntry>()

    private lateinit var anyChartView:AnyChartView
    private var datosEstadistica: MutableList<DataEntry> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        estadisticasViewModel =
            ViewModelProvider(this).get(EstadisticasViewModel::class.java)

        _binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //binding.cartTiposRecordatorios.visibility = View.INVISIBLE
        binding.tvNoDatos.visibility=View.INVISIBLE
        binding.pbEstadisticas.visibility = View.VISIBLE
        pie = AnyChart.pie3d()

        estadisticasViewModel.text.observe(viewLifecycleOwner, Observer {

            anyChartView = binding.cartTiposMetas
            anyChartView.setChart(pie)

        })
        configurarObservadores()
        configuradorEventos()

        return root
    }
    private fun configurarObservadores() {
        opcionEstadistica = binding.spOpcionEstadistica
        opcionEstadistica.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, opcionesEstadisticas)

    }
    private fun configuradorEventos() {

        opcionEstadistica.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                resultadoEstadistica = opcionesEstadisticas.get(p2)
                leerTiposMetas()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("Selecciona una opción")

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun crearGrafica(tipoEstadistica: String){

            if(datosEstadistica.isNotEmpty()) {
                try {
                    binding.tvNoDatos.visibility=View.INVISIBLE
                    pie.data(datosEstadistica)
                    pie.title("Monto total por " + tipoEstadistica)

                }
                catch (e:Exception){
                    println(e.message)
                }

            }
            else{
                binding.tvNoDatos.visibility=View.VISIBLE
            }
            binding.pbEstadisticas.visibility=View.INVISIBLE

    }


    fun leerTiposMetas(){
        val database = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        var tipoEstadistica = ""

        when (resultadoEstadistica) {
            "TIPOS DE METAS" -> tipoEstadistica = "/Metas"
            "TIPOS DE RECORDATORIOS" -> tipoEstadistica = "/Recordatorios"
        }
        val myRef =database.getReference(uid + tipoEstadistica)

        myRef.addValueEventListener(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    when (resultadoEstadistica) {
                        "TIPOS DE METAS" -> {
                            datosEstadistica.clear()
                            val numbersMap = mutableMapOf<String, Double>()
                            for (registro in snapshot.children) {
                                val monto = registro.child("montoReal").getValue(Double::class.java)
                                val tipo = registro.child("tipo").getValue(String::class.java)
                                if (numbersMap.containsKey(tipo.toString())) {
                                    numbersMap[tipo.toString()] =
                                        numbersMap.getValue(tipo.toString()) + monto!!
                                } else {
                                    numbersMap[tipo.toString()] = monto!!
                                }

                            }
                            numbersMap.forEach { tipo, monto ->
                                datosEstadistica.add(ValueDataEntry(tipo, monto))
                            }
                            crearGrafica("tipo de metas")

                        }
                        "TIPOS DE RECORDATORIOS" -> {
                            datosEstadistica.clear()
                            val numbersMap = mutableMapOf<String, Double>()
                            for (registro in snapshot.children) {
                                val monto = registro.child("cantidadPago").getValue(Double::class.java)
                                val tipo = registro.child("tipo").getValue(String::class.java)
                                if (numbersMap.containsKey(tipo.toString())) {
                                    numbersMap[tipo.toString()] =
                                        numbersMap.getValue(tipo.toString()) + monto!!
                                } else {
                                    numbersMap[tipo.toString()] = monto!!
                                }

                            }
                            numbersMap.forEach { tipo, monto ->
                                datosEstadistica.add(ValueDataEntry(tipo, monto))
                            }

                            crearGrafica("recordatorios")
                        }

                    }


                }
                catch (e:Exception){
                    println(e.message)
                }


            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

}
