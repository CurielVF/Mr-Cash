package mx.itesm.cerco.proyectofinal.ui.estadisticas

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var estadisticasViewModel: EstadisticasViewModel
    private var _binding: FragmentEstadisticasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var tiposMetas : MutableList<ValueDataEntry> = mutableListOf<ValueDataEntry>()

    private lateinit var anyChartView:AnyChartView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        estadisticasViewModel =
            ViewModelProvider(this).get(EstadisticasViewModel::class.java)

        _binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tvNoDatos.visibility=View.INVISIBLE
        binding.pbEstadisticas.visibility = View.VISIBLE

        estadisticasViewModel.text.observe(viewLifecycleOwner, Observer {

            anyChartView = binding.cartTiposMetas
            leerTiposMetas()


        })

        configuradorEventos()

        return root
    }

    private fun configuradorEventos() {
        binding.btnRecordatorios.setOnClickListener {
            val accion = EstadisticasFragmentDirections.actionNavigationNotificationsToEstadisticasDetail()
            this.findNavController()?.navigate(accion)
            

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun crearGraficaTipoMetas(dataEntries: MutableList<DataEntry>){

            if(dataEntries.isNotEmpty()) {
                try {
                    binding.tvNoDatos.visibility=View.INVISIBLE
                    val pie = AnyChart.pie3d()
                    pie.data(dataEntries)
                    pie.title("Monto total por tipo de meta")
                    anyChartView.setChart(pie)

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
        val myRef =database.getReference(uid+"/Metas")

        myRef.addValueEventListener(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    tiposMetas.clear()
                    val dataEntries: MutableList<DataEntry> = ArrayList()
                    val numbersMap = mutableMapOf<String, Double>()
                    for (registro in snapshot.children) {
                        val monto = registro.child("precio").getValue(Double::class.java)
                        val tipo = registro.child("tipo").getValue(String::class.java)
                        if (numbersMap.containsKey(tipo.toString())) {
                            numbersMap[tipo.toString()] =
                                numbersMap.getValue(tipo.toString()) + monto!!
                        } else {
                            numbersMap[tipo.toString()] = monto!!
                        }

                    }
                    numbersMap.forEach { tipo, monto ->
                        dataEntries.add(ValueDataEntry(tipo, monto))
                    }
                    crearGraficaTipoMetas(dataEntries)

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
