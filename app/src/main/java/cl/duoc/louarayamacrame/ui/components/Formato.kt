package cl.duoc.louarayamacrame.ui.components

/** Precio en pesos chilenos con separador de miles: 120000 -> "$120.000". */
fun formatoPrecio(valor: Int): String {
    val texto = valor.toString()
    val conPuntos = StringBuilder()
    texto.forEachIndexed { indice, caracter ->
        if (indice > 0 && (texto.length - indice) % 3 == 0) conPuntos.append('.')
        conPuntos.append(caracter)
    }
    return "$" + conPuntos.toString()
}

/** Código visible de una solicitud: id 13 -> "#SOL-0013". */
fun codigoSolicitud(id: Int): String = "#SOL-" + id.toString().padStart(4, '0')
