package mx.itesm.cerco.proyectofinal.ui.model

import java.util.*

data class Recordatorio(
    var nombrePago: String?,
    var fechaPago: String?,
    var cantidadPago: Double?,
    var tipo: String?,
    var hora: String?,
    var id: String?=null,
    var uuidRecordatorio: String? = null,
    var frecuencia: String?)