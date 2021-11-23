package mx.itesm.cerco.proyectofinal.ui.tips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.ui.view.RenglonListener
import mx.itesm.cerco.proyectofinal.ui.tips.model.Tip

class AdaptadorListaTips(var arrTip: ArrayList<Tip>) :
    RecyclerView.Adapter<AdaptadorListaTips.TipsViewHolder> ()
{
    class TipsViewHolder (vista: View) : RecyclerView.ViewHolder(vista) {
        //Titulo del Tip
        private val tvTip = vista.findViewById<TextView>(R.id.tvTitulo)
        //summary del tip
        private val tvExtractTip = vista.findViewById<TextView>(R.id.tvExtractTip)

        fun setData (tip: Tip){
            tvTip.text= tip.titulo
            tvExtractTip.text = tip.fragContenido
        }
    }

    var listener : RenglonListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        val vistaRenglon = LayoutInflater.from(parent.context).inflate(R.layout.renglon_tip, parent, false)

        return TipsViewHolder(vistaRenglon)
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        holder.setData(arrTip[position])

        val renglon = holder.itemView.findViewById<LinearLayout>(R.id.layoutRenglon)
        renglon.setOnClickListener {
            listener?.clickEnRenglon(position)
        }
    }

    override fun getItemCount(): Int {
        return arrTip.size
    }

    fun actualizarDatos(lista: List<Tip>?){
        arrTip.clear()
        if(lista != null){
            arrTip.addAll(lista)
        }
        notifyDataSetChanged()
    }

}
