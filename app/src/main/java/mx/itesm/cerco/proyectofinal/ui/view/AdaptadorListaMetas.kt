package mx.itesm.cerco.proyectofinal.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.ui.model.Meta

class AdaptadorListaMetas (var arrMetas: ArrayList<Meta>):
    RecyclerView.Adapter<AdaptadorListaMetas.MetaViewHolder>()
{

    //Regresa los renglones o cajas cuando es necesario
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetaViewHolder {
        //Cada renglon se crea aquí
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.renglon_meta,parent,false)
        return MetaViewHolder(vista)
    }


    override fun onBindViewHolder(holder: MetaViewHolder, position: Int) {
        holder.set(arrMetas[position])
    }

    //Tamaño del arreglo de los renglones
    override fun getItemCount(): Int {
        return arrMetas.size
    }

    fun actualizar(lista: List<Meta>?) {
        arrMetas.clear() //Manejo de memoria optimo
        if(lista!= null){
            arrMetas.addAll(lista)
        }
        notifyDataSetChanged() //Recargar la información
    }

    //Vista del renglón
    class MetaViewHolder(vista : View) : RecyclerView.ViewHolder(vista) {
        private val tvNombreMeta=vista.findViewById<TextView>(R.id.tvNombreMeta)
        private val tvPrecio=vista.findViewById<TextView>(R.id.tvPrecioMeta)
        private val tvFechaLimite=vista.findViewById<TextView>(R.id.tvFechaLimite)

        fun set(meta : Meta){
            tvNombreMeta.text=meta.nombre
            tvPrecio.text="Costo total: " + meta.precio.toString()
            tvFechaLimite.text="Fecha de término: " + meta.fechaLimite
        }

    }

}