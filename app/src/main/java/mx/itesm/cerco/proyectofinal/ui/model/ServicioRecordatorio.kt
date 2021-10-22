package mx.itesm.cerco.proyectofinal.ui.model

//
class ServicioRecordatorio {

    fun leerRecordatorio(): List<Recordatorio>{
        return listOf(
            Recordatorio("Pago de Agua","14/10/2021",234.50),
            Recordatorio("Pago de Luz","10/11/2021",200.50),
            Recordatorio("Pago de Netflix","14/10/2021",350.34)
        )
    }
}
