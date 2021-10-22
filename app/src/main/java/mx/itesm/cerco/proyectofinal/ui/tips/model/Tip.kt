package mx.itesm.cerco.proyectofinal.ui.tips.model

import java.io.Serializable

data class Tip(
    // del campo JSON
    val titulo: String,
    val fragContenido: String,
    val URL: String

): Serializable
