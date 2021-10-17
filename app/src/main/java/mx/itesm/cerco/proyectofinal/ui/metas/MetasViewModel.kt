package mx.itesm.cerco.proyectofinal.ui.metas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mx.itesm.cerco.proyectofinal.ui.model.Meta

class MetasViewModel : ViewModel() {

    val arrMetas =MutableLiveData<List<Meta>>()

    fun setMetas(metas:List<Meta>){
        arrMetas.value=metas
    }
}