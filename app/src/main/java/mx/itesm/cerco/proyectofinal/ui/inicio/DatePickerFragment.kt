package mx.itesm.cerco.proyectofinal.ui.inicio

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.RequiresApi

import androidx.fragment.app.DialogFragment

//Permite mostrar los dialogos para la fecha, Espera un dia, mes y año
class DatePickerFragment(val listener: (dia:Int, mes:Int, año:Int)-> Unit): DialogFragment(),
DatePickerDialog.OnDateSetListener{

    //Muestra el calendario

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendario=Calendar.getInstance()
        val dia=calendario.get(Calendar.DAY_OF_MONTH)
        val mes=calendario.get(Calendar.MONTH)
        val año=calendario.get(Calendar.YEAR)

        val picker=DatePickerDialog(activity as Context,this,año,mes,dia)
        return picker

    }

    //Avisa cuando seleccionamos una fecha en el calendario
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth,month,year)
    }
}