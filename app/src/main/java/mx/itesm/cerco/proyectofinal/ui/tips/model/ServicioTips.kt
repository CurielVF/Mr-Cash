package mx.itesm.cerco.proyectofinal.ui.tips.model

class ServicioTips {

    fun leerTips(): List<Tip> {
        return arrayListOf(
            Tip("Registre sus gastos",
                "El primer paso para comenzar a ahorrar dinero es determinar cuánto gasta." +
                        " Haga un seguimiento de todos sus gastos; eso quiere decir cada taza de café, " +
                        "artículo para el hogar y propina en efectivo.", "Conenido completo"),

            Tip("Haga un presupuesto para ahorros", "Una vez que tenga una idea de cuánto gasta en un mes," +
                    " puede comenzar a organizar los gastos que registró y establecer un presupuesto con el que pueda vivir.", "contenido completo"),

            Tip("Encuentre maneras de recortar gastos", "Si sus gastos son tan altos que no puede ahorrar como quisiera," +
                    " es posible que sea el momento de recortar gastos.", "contenido completo"),

            Tip("Establezca metas de ahorros", "Una de las mejores formas para ahorrar dinero es establecer una meta." +
                    " Empiece por pensar para qué podría querer ahorrar, tal vez va a casarse o está planeando unas vacaciones; a corto y largo plazo.", "contenido completo"),

            Tip("Decida prioridades", "Después de sus gastos e ingresos, es probable que sus metas tengan" +
                    " el mayor impacto en cómo distribuye sus ahorros.", "contenido completo"),

            Tip("Elija heramientas adecuadas", "Si está ahorrando para metas a corto plazo, considere usar estas cuentas de depósito aseguradas", "contenido completo"),

            Tip("Ahorros automaticos", "Casi todos los bancos ofrecen transferencias automatizadas entre sus cuentas de cheques y de ahorros. Usted puede elegir cuándo," +
                    " cuánto y a dónde transferir dinero, e incluso dividir su depósito directo", "contenido completo"),

            Tip("Vea crecer sus ahorros", "Revise su presupuesto y vea su progreso cada mes. Esto no solamente le ayudará a apegarse a su plan personal de ahorros," +
                    " sino que también le ayudará a identificar y corregir rápidamente cualquier problema.", "contenido completo")

        )
    }

}