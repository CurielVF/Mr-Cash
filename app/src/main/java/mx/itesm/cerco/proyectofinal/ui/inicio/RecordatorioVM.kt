package mx.itesm.cerco.proyectofinal.ui.inicio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mx.itesm.cerco.proyectofinal.ui.model.Recordatorio

class RecordatorioVM : ViewModel() {
    val arrRecordatorios = MutableLiveData<List<Recordatorio>>()

    fun setRecordatorios(recordatorios:List<Recordatorio>){
        arrRecordatorios.value=recordatorios
    }
}