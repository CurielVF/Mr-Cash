package mx.itesm.cerco.proyectofinal.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.ui.model.Recordatorio

//Proporcionadatos para el RecycleView
class AdaptadorListaRecordatorio (var arrRecordatorio: ArrayList<Recordatorio>):
RecyclerView.Adapter<AdaptadorListaRecordatorio.RecordatorioViewHolder>()
{

    //Regresa los renglones o cajas cuando es necesario
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordatorioViewHolder {
        //Cada renglon se crea aquí
        val vista =LayoutInflater.from(parent.context).inflate(R.layout.reglon_recordatorio,parent,false)
        return RecordatorioViewHolder(vista)
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

    //Vista del renglón delrecordatorio
    class RecordatorioViewHolder(vista : View) : RecyclerView.ViewHolder(vista) {
        private val tvNombrePago=vista.findViewById<TextView>(R.id.tvNombrePago)

        fun set(recordatorio : Recordatorio){
            tvNombrePago.text=recordatorio.nombrePago
        }

    }
}