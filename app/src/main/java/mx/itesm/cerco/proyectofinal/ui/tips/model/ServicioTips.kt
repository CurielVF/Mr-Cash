package mx.itesm.cerco.proyectofinal.ui.tips.model

class ServicioTips {
    // en manejo de textos
    fun leerTips(): List<Tip> {
        return arrayListOf(
            Tip("8 Tips para ahorrar",
                "El primer paso para comenzar a ahorrar dinero es determinar cuánto gasta..."
                        , "https://bettermoneyhabits.bankofamerica.com/es/saving-budgeting/ways-to-save-money"),


        )
    }

}