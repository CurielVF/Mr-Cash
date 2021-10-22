package mx.itesm.cerco.proyectofinal.ui.tips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mx.itesm.cerco.proyectofinal.ui.tips.model.ServicioTips
import mx.itesm.cerco.proyectofinal.ui.tips.model.Tip

class TipsViewModel : ViewModel() {

    private val servicioTips = ServicioTips()
    val arrTips = MutableLiveData<List<Tip>>()

    //Eventos
    fun leerTips(){
        arrTips.value = servicioTips.leerTips()
    }

}