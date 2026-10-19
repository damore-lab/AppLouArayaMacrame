package cl.duoc.louarayamacrame.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.louarayamacrame.validation.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _correo = MutableStateFlow("")
    val correo = _correo.asStateFlow()

    private val _contrasena = MutableStateFlow("")
    val contrasena = _contrasena.asStateFlow()

    private val _errorCorreo = MutableStateFlow("")
    val errorCorreo = _errorCorreo.asStateFlow()

    private val _errorContrasena = MutableStateFlow("")
    val errorContrasena = _errorContrasena.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    fun actualizarCorreo(valor: String) {
        _correo.value = valor
        _errorCorreo.value = ""
        _mensaje.value = ""
    }

    fun actualizarContrasena(valor: String) {
        _contrasena.value = valor
        _errorContrasena.value = ""
        _mensaje.value = ""
    }

    fun validarFormulario(): Boolean {

        _errorCorreo.value = ""
        _errorContrasena.value = ""
        _mensaje.value = ""

        val correoIngresado = _correo.value.trim()

        if (correoIngresado.isEmpty()) {
            _errorCorreo.value = "El correo es obligatorio"
        } else if (!Validaciones.correoValido(correoIngresado)) {
            _errorCorreo.value = "Ingresa un correo válido"
        }

        if (!Validaciones.contrasenaLoginValida(_contrasena.value)) {
            _errorContrasena.value = "La contraseña es obligatoria"
        }

        val esValido = _errorCorreo.value.isEmpty() &&
                _errorContrasena.value.isEmpty()

        if (esValido) {
            _mensaje.value =
                "Formulario válido. Autenticación pendiente."
        }

        return esValido
    }
}
