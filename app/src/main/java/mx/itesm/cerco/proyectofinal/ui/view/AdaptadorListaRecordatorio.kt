package mx.itesm.cerco.proyectofinal.ui.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.ui.inicio.NotificacionWorkManager
import mx.itesm.cerco.proyectofinal.ui.model.Recordatorio
import java.util.*
import kotlin.collections.ArrayList

//Proporcionadatos para el RecycleView
class AdaptadorListaRecordatorio (var arrRecordatorio: ArrayList<Recordatorio>):
RecyclerView.Adapter<AdaptadorListaRecordatorio.RecordatorioViewHolder>()
{
    private lateinit var context: Context
    //Regresa los renglones o cajas cuando es necesario
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordatorioViewHolder {
        //Cada renglon se crea aquí
        val vista =LayoutInflater.from(parent.context).inflate(R.layout.reglon_recordatorio,parent,false)
        context = parent.getContext();
        return RecordatorioViewHolder(vista,context)

    }


    override fun onBindViewHolder(holder: RecordatorioViewHolder, position: Int) {
       holder.set(arrRecordatorio[position])
    }


    //Tamaño del arreglo de los renglones
    override fun getItemCount(): Int {
       return arrRecordatorio.size
    }

    fun actualizar(lista: List<Recordatorio>?) {
        arrRecordatorio.clear() //Manejo de memoria optimo
        if(lista!= null){
            arrRecordatorio.addAll(lista)
        }
        notifyDataSetChanged() //Recargar la información
    }

    //Vista del renglón del recordatorio
    class RecordatorioViewHolder(vista : View,context:Context) : RecyclerView.ViewHolder(vista) {
        private val tvNombrePago=vista.findViewById<TextView>(R.id.tvNombreRecordatorio)
        private val tvFecha = vista.findViewById<TextView>(R.id.tvFechaRecordatorio)
        private val tvHora = vista.findViewById<TextView>(R.id.tvHoraRecordatorio)
        private val tvMonto = vista.findViewById<TextView>(R.id.tvMontoRecordatorio)
        private val btnEliminar = vista.findViewById<ImageButton>(R.id.btnEliminarRecordatorio)
        private val context=context
        fun set(recordatorio : Recordatorio){
            btnEliminar.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(context)

                dialogBuilder.setMessage("¿Estás seguro de que quieres borrar el recordatorio?")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", DialogInterface.OnClickListener {
                            dialog, id ->
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        val database = FirebaseDatabase.getInstance()
                        val myRef =database.getReference(uid+"/Recordatorios/"+recordatorio.id)
                        myRef.removeValue()
                        println("id recordatorio"+recordatorio.id)


                        val uuidRec:UUID = UUID.fromString(recordatorio.uuidRecordatorio)
                        val instanceWorkManager = WorkManager.getInstance(context)


                        instanceWorkManager.cancelWorkById(uuidRec)
                        Toast.makeText(context,"Recordatorio eliminado correctamente", Toast.LENGTH_SHORT).show()
                    })
                    // negative button text and action
                    .setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })
                val alert = dialogBuilder.create()
                alert.setTitle("Advertencia")
                alert.show()
            }
            tvNombrePago.text = recordatorio.nombrePago
            tvFecha.text = "Fecha de pago: " + recordatorio.fechaPago
            tvHora.text = "Hora de pago: " + recordatorio.hora
            tvMonto.text = "Monto a pagar: $" + String.format("%.2f", recordatorio.cantidadPago)
        }
    }
}