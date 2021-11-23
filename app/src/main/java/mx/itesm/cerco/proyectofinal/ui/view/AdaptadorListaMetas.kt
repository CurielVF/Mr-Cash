package mx.itesm.cerco.proyectofinal.ui.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.ui.Constantes
import mx.itesm.cerco.proyectofinal.ui.model.Meta
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class AdaptadorListaMetas (var arrMetas: ArrayList<Meta>):
    RecyclerView.Adapter<AdaptadorListaMetas.MetaViewHolder>()
{
    //Listener. Es un objeto que escucha eventos de Adaptador
    var listener : RenglonListener? = null
    private lateinit var context: Context
    //Regresa los renglones o cajas cuando es necesario
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetaViewHolder {
        //Cada renglon se crea aquí
        val vistaRenglon = LayoutInflater.from(parent.context)
            .inflate(R.layout.renglon_meta,parent,false)
        context = parent.getContext();
        return MetaViewHolder(vistaRenglon, context)
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
    class MetaViewHolder(vista: View, context: Context) : RecyclerView.ViewHolder(vista) {
        private val tvNombreMeta=vista.findViewById<TextView>(R.id.tvNombreMeta)
        private val tvPrecio=vista.findViewById<TextView>(R.id.tvPrecioMeta)
        private val tvFechaLimite=vista.findViewById<TextView>(R.id.tvFechaLimite)
        private val tvAhorroNecesario=vista.findViewById<TextView>(R.id.tvAhorroNecesario)
        private val tvDiasRestantes=vista.findViewById<TextView>(R.id.tvDiasRestantes)
        private val ivTipoMeta=vista.findViewById<ImageView>(R.id.ivTipoMeta)
        private val cvMeta=vista.findViewById<CardView>(R.id.cvMeta)
        private val btnEliminar=vista.findViewById<ImageButton>(R.id.btnEliminarMeta)
        private val context=context
        @RequiresApi(Build.VERSION_CODES.O)
        fun set(meta: Meta){
            val añosRestantes = meta.periodo?.years
            val mesesRestantes = meta.periodo?.months
            val diasRestantes = meta.periodo?.days
            val totalDias = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(meta.fechaLimite))
            val tiempoRestante = obtenerTiempoRestante(añosRestantes,mesesRestantes,diasRestantes,totalDias)

            tvNombreMeta.text=meta.nombre
            tvPrecio.text="Costo total: $" + String.format("%.2f", meta.precio)
            tvFechaLimite.text="Fecha de término: " + meta.fechaLimite
            tvAhorroNecesario.text="Ahorro diario: $" + String.format("%.2f", meta.ahorroNecesario)
            tvDiasRestantes.text= tiempoRestante
            val colorEstado = obtenerEstadoColor(meta.montoReal!!,meta.precio!!,meta.fechaLimite!!)
            cvMeta.setCardBackgroundColor(Color.parseColor(colorEstado))
            when (meta.tipo) {
                "ENTRETENIMIENTO" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_entretenimiento)
                "HOGAR" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_hogar)
                "COMIDA" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_comida)
                "OTRO" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_otro)
                "PERSONAL" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_personal)
                "VEHICULO" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_vehiculo)
                "VIAJES" -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_viaje)
                else -> ivTipoMeta.setImageResource(R.drawable.ic_tipo_otro)
            }

            btnEliminar.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(context)

                dialogBuilder.setMessage("¿Estás seguro de que quieres borrar la meta?")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", DialogInterface.OnClickListener {
                            dialog, id ->
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        val database = FirebaseDatabase.getInstance()
                        val myRef =database.getReference(uid+"/Metas/"+meta.llaveMeta)
                        myRef.removeValue()
                        Toast.makeText(context,"Meta eliminada correctamente", Toast.LENGTH_SHORT).show()
                    })
                    // negative button text and action
                    .setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })
                val alert = dialogBuilder.create()
                alert.setTitle("Advertencia")
                alert.show()
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun obtenerEstadoColor(
            montoReal: Double,
            montoMeta: Double,
            fechaLimite: String
        ): String? {
            var colorEstado = Constantes.color_progreso
            if(montoReal >= montoMeta){
                colorEstado = Constantes.color_completado
            }
            else{

                val diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(fechaLimite))
                if (diasRestantes < 0){
                    colorEstado = Constantes.color_retraso
                }

            }

            return colorEstado

        }

        private fun obtenerTiempoRestante(
            añosRestantes: Int?,
            mesesRestantes: Int?,
            diasRestantes: Int?,
            totalDias: Long
        ): String {
            var añosRString = ""
            var mesesRString = ""
            var diasRString = ""

            if (totalDias!! >= 0) {
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
            }
            else{
                diasRString =
                    if (-1.0 == totalDias.toDouble() ) "Retrasado 1 día" else "Retrasado " + (-totalDias).toString() + " días"
            }

            val tiempoRestante = añosRString + mesesRString + diasRString
            return tiempoRestante
        }

    }


}