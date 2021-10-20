package mx.itesm.cerco.proyectofinal.ui.model

import java.time.LocalDate
import java.time.Period

data class Meta (var nombre : String?,var fechaLimite: String?, var precio: Double?,
                 var tipo : String?, var periodo : Period? = null, var ahorroNecesario : Double? = null,
                 var fechaCreacion: String?)
//, var diasRestantes : Period?, var ahorroNecesario : Double?