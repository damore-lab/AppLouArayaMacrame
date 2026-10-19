package cl.duoc.louarayamacrame.validation

object Validaciones {

    fun nombreValido(nombre: String): Boolean {
        return nombre.isNotBlank()
    }

    fun correoValido(correo: String): Boolean {
        val patron = Regex(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
        return patron.matches(correo.trim())
    }

    fun contrasenaLoginValida(contrasena: String): Boolean {
        return contrasena.isNotEmpty()
    }

    fun contrasenaRegistroValida(contrasena: String): Boolean {
        return contrasena.length >= 8
    }

    fun contrasenasCoinciden(
        contrasena: String,
        confirmacion: String
    ): Boolean {
        return contrasena == confirmacion &&
                confirmacion.isNotEmpty()
    }
}
