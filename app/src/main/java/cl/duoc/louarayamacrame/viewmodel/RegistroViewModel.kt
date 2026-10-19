package cl.duoc.louarayamacrame.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.louarayamacrame.validation.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegistroViewModel : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre = _nombre.asStateFlow()

    private val _correo = MutableStateFlow("")
    val correo = _correo.asStateFlow()

    private val _contrasena = MutableStateFlow("")
    val contrasena = _contrasena.asStateFlow()

    private val _confirmacion = MutableStateFlow("")
    val confirmacion = _confirmacion.asStateFlow()

    private val _errorNombre = MutableStateFlow("")
    val errorNombre = _errorNombre.asStateFlow()

    private val _errorCorreo = MutableStateFlow("")
    val errorCorreo = _errorCorreo.asStateFlow()

    private val _errorContrasena = MutableStateFlow("")
    val errorContrasena = _errorContrasena.asStateFlow()

    private val _errorConfirmacion = MutableStateFlow("")
    val errorConfirmacion = _errorConfirmacion.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    fun actualizarNombre(valor: String) {
        _nombre.value = valor
        _errorNombre.value = ""
        _mensaje.value = ""
    }

    fun actualizarCorreo(valor: String) {
        _correo.value = valor
        _errorCorreo.value = ""
        _mensaje.value = ""
    }

    fun actualizarContrasena(valor: String) {
        _contrasena.value = valor
        _errorContrasena.value = ""
        _errorConfirmacion.value = ""
        _mensaje.value = ""
    }

    fun actualizarConfirmacion(valor: String) {
        _confirmacion.value = valor
        _errorConfirmacion.value = ""
        _mensaje.value = ""
    }

    fun validarFormulario(): Boolean {

        _errorNombre.value = ""
        _errorCorreo.value = ""
        _errorContrasena.value = ""
        _errorConfirmacion.value = ""
        _mensaje.value = ""

        val nombreIngresado = _nombre.value.trim()
        val correoIngresado = _correo.value.trim()

        // Validación del nombre
        if (!Validaciones.nombreValido(nombreIngresado)) {
            _errorNombre.value = "El nombre es obligatorio"
        }

        // Validación del correo
        if (correoIngresado.isEmpty()) {
            _errorCorreo.value = "El correo es obligatorio"
        } else if (!Validaciones.correoValido(correoIngresado)) {
            _errorCorreo.value = "Ingresa un correo válido"
        }

        // Validación de la contraseña
        if (_contrasena.value.isEmpty()) {
            _errorContrasena.value = "La contraseña es obligatoria"
        } else if (!Validaciones.contrasenaRegistroValida(_contrasena.value)) {
            _errorContrasena.value =
                "La contraseña debe tener al menos 8 caracteres"
        }

        // Validación de confirmación
        if (_confirmacion.value.isEmpty()) {
            _errorConfirmacion.value =
                "Debes confirmar tu contraseña"
        } else if (!Validaciones.contrasenasCoinciden(
                _contrasena.value,
                _confirmacion.value
            )
        ) {
            _errorConfirmacion.value =
                "Las contraseñas no coinciden"
        }

        val esValido = _errorNombre.value.isEmpty() &&
                _errorCorreo.value.isEmpty() &&
                _errorContrasena.value.isEmpty() &&
                _errorConfirmacion.value.isEmpty()

        if (esValido) {
            _mensaje.value =
                "Formulario válido. Registro pendiente."
        }

        return esValido
    }
}
