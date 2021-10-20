package mx.itesm.cerco.proyectofinal.ui.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
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


    @RequiresApi(Build.VERSION_CODES.O)
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
        private val tvAhorroNecesario=vista.findViewById<TextView>(R.id.tvAhorroNecesario)
        private val tvDiasRestantes=vista.findViewById<TextView>(R.id.tvDiasRestantes)

        @RequiresApi(Build.VERSION_CODES.O)
        fun set(meta : Meta){
            val añosRestantes = meta.periodo?.years
            val mesesRestantes = meta.periodo?.months
            val diasRestantes = meta.periodo?.days

            val tiempoRestante = obtenerTiempoRestante(añosRestantes,mesesRestantes,diasRestantes)

            tvNombreMeta.text=meta.nombre
            tvPrecio.text="Costo total: $" + String.format("%.2f", meta.precio)
            tvFechaLimite.text="Fecha de término: " + meta.fechaLimite
            tvAhorroNecesario.text="Ahorro por semana: $" + String.format("%.2f", meta.ahorroNecesario)
            tvDiasRestantes.text= tiempoRestante
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