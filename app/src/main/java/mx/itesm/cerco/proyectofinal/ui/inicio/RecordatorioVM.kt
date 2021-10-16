package mx.itesm.cerco.proyectofinal.ui.inicio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mx.itesm.cerco.proyectofinal.ui.model.Recordatorio
import mx.itesm.cerco.proyectofinal.ui.model.ServicioRecordatorio

class RecordatorioVM : ViewModel() {
    private val model= ServicioRecordatorio()

    val arrRecoratorios =MutableLiveData<List<Recordatorio>>()

    fun leerDatos(){
        arrRecoratorios.value =model.leerRecordatorio()
    }
}