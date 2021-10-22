package mx.itesm.cerco.proyectofinal.ui.tips.model

class ServicioTips {
    // en manejo de textos
    fun leerTips(): List<Tip> {
        return arrayListOf(
            Tip("8 Tips para ahorrar",
                "Bank Of America nos regala 8 consejos..."
                        , "https://bettermoneyhabits.bankofamerica.com/es/saving-budgeting/ways-to-save-money"),
            Tip("7 consejos básicos para finanzas personales",
            "Entrepreneur nos da 7 consejos muy utiles...", "https://www.entrepreneur.com/article/268900"),
            Tip("La Mejores SOFIPOS en 2021", "¿Quieres adentrarte mas sobre la educacion financiera?",
                    "https://omareducacionfinanciera.com/mejores-sofipos-2021/"),
            Tip("Los 10 Mejores ETF's para invertir", "¿Quieres invertir en bolsa? ve a lo seguro...",
            "https://omareducacionfinanciera.com/mejores-sofipos-2021/"),
            Tip("¿Cómo empezar a invertir en acciones?", "Bueno, hay que empezar con el pie derecho y tienes poca experiecia, bueno, no es necesaria",
            "https://cnnespanol.cnn.com/2021/02/02/invertir-bolsa-sin-experiencia/")


        )
    }

}