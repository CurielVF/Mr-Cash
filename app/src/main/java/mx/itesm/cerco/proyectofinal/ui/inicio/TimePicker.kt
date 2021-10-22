package mx.itesm.cerco.proyectofinal.ui.inicio

import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentOnAttachListener

//Permite mostrar los dialogos para la hora, espera una hora y un minuto
class TimePicker(val listener: (hora: Int, minuto: Int) -> Unit) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {

    //Muestra el Muestra el Reloj
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendario= Calendar.getInstance()
        val hora=calendario.get(Calendar.HOUR_OF_DAY)
        val minuto= calendario.get(Calendar.MINUTE)

        return TimePickerDialog(activity,this,hora,minuto,DateFormat.is24HourFormat(activity))
    }

    //Se ejecuta cuando se selecciona la hora
    override fun onTimeSet(p0: TimePicker?, hora: Int, minuto: Int) {
       listener(hora,minuto)
    }
}