package mx.itesm.cerco.proyectofinal.ui.model

import java.io.Serializable
import java.time.LocalDate
import java.time.Period

data class Meta (
    var nombre : String?,
    var fechaLimite: String?,
    var precio: Double?,
    var tipo : String?,
    var periodo : Period? = null,
    var ahorroNecesario : Double? = null,
    var fechaCreacion: String?,
    var montoReal : Double? = 0.0,
    var llaveMeta : String? = null,) : Serializable

//, var diasRestantes : Period?, var ahorroNecesario : Double?