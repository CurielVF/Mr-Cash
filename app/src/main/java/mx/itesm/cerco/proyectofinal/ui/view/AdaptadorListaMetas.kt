package mx.itesm.cerco.proyectofinal.ui.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.ui.metas.MetasFragment
import mx.itesm.cerco.proyectofinal.ui.metas.MetasFragmentDirections
import mx.itesm.cerco.proyectofinal.ui.model.Meta
import mx.itesm.cerco.proyectofinal.ui.tips.AdaptadorListaTips
import mx.itesm.cerco.proyectofinal.ui.tips.model.Tip

class AdaptadorListaMetas (var arrMetas: ArrayList<Meta>):
    RecyclerView.Adapter<AdaptadorListaMetas.MetaViewHolder>()
{
    //Listener. Es un objeto que escucha eventos de Adaptador
    var listener : RenglonListener? = null

    //Regresa los renglones o cajas cuando es necesario
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetaViewHolder {
        //Cada renglon se crea aquí
        val vistaRenglon = LayoutInflater.from(parent.context)
            .inflate(R.layout.renglon_meta,parent,false)
        return MetaViewHolder(vistaRenglon)  //Una caja con ese renglón
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MetaViewHolder, position: Int) {
        holder.set(arrMetas[position])

        val renglon = holder.itemView.findViewById<LinearLayout>(R.id.layoutRenglon)
        renglon.setOnClickListener{
            println("Click sobre: ${arrMetas[position]}")
            //Hacer el cambio de pantalla ???? interfaces... listener...
            listener?.clickEnRenglon(position)
        }
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
        private val tvAhorroNecesario=vista.findViewById<TextView>(R.id.tvAhorroNecesario)
        private val tvDiasRestantes=vista.findViewById<TextView>(R.id.tvDiasRestantes)
        private val ivTipoMeta=vista.findViewById<ImageView>(R.id.ivTipoMeta)


        @RequiresApi(Build.VERSION_CODES.O)
        fun set(meta : Meta){
            val añosRestantes = meta.periodo?.years
            val mesesRestantes = meta.periodo?.months
            val diasRestantes = meta.periodo?.days

            val tiempoRestante = obtenerTiempoRestante(añosRestantes,mesesRestantes,diasRestantes)

            tvNombreMeta.text=meta.nombre
            tvPrecio.text="Costo total: $" + String.format("%.2f", meta.precio)
            tvFechaLimite.text="Fecha de término: " + meta.fechaLimite
            tvAhorroNecesario.text="Ahorro diario: $" + String.format("%.2f", meta.ahorroNecesario)
            tvDiasRestantes.text= tiempoRestante

            when (meta.tipo) {
                "ENTRETENIMIENTO" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_entretenimiento)
                "HOGAR" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_hogar)
                "COMIDA" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_comida)
                "OTRO" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_otro)
                else -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_otro)
            }
        }

        private fun obtenerTiempoRestante(
            añosRestantes: Int?,
            mesesRestantes: Int?,
            diasRestantes: Int?
        ): String {
            var añosRString = ""
            var mesesRString = ""
            var diasRString = ""

            if (añosRestantes != 0) {
                añosRString =
                    if (añosRestantes == 1) "Queda 1 año" else "Quedan " + añosRestantes.toString() + " años"
                if (mesesRestantes != 0) {
                    if (diasRestantes != 0) {
                        mesesRString =
                            if (mesesRestantes == 1) ", 1 mes" else ", " + mesesRestantes.toString() + " meses"
                        diasRString =
                            if (diasRestantes == 1) " y 1 día" else " y " + diasRestantes.toString() + " días"
                    } else {
                        mesesRString =
                            if (mesesRestantes == 1) " y 1 mes" else " y " + mesesRestantes.toString() + " meses"
                    }
                } else if (diasRestantes != 0) {
                    diasRString =
                        if (diasRestantes == 1) " y 1 día" else " y " + diasRestantes.toString() + " días"
                }
            } else if (mesesRestantes != 0) {
                mesesRString =
                    if (mesesRestantes == 1) "Queda 1 mes" else "Quedan " + mesesRestantes.toString() + " meses"
                if (diasRestantes != 0) {
                    diasRString =
                        " y " + if (diasRestantes == 1) "1 día" else diasRestantes.toString() + " días"
                }
            } else {
                diasRString =
                    if (diasRestantes == 1) "Queda 1 día" else "Quedan " + diasRestantes.toString() + " días"
            }

            val tiempoRestante = añosRString + mesesRString + diasRString
            return tiempoRestante
        }

    }

}