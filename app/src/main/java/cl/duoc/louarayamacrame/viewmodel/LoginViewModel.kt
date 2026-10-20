package cl.duoc.louarayamacrame.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.louarayamacrame.validation.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import cl.duoc.louarayamacrame.data.UsuarioDao
import cl.duoc.louarayamacrame.data.SeguridadContrasena
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    private var usuarioDao: UsuarioDao? = null

    fun configurarDao(dao: UsuarioDao) {
        usuarioDao = dao
    }

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
    private val _usuarioId = MutableStateFlow<Int?>(null)
    val usuarioId = _usuarioId.asStateFlow()

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


    fun iniciarSesion() {
        _usuarioId.value = null
        if (!validarFormulario()) return

        val dao = usuarioDao ?: run {
            _mensaje.value = "No se pudo acceder a la base de datos"
            return
        }

        viewModelScope.launch {
            try {
                val correoIngresado = _correo.value.trim().lowercase()
                val contrasenaIngresada = _contrasena.value

                val usuario = withContext(Dispatchers.IO) {
                    dao.buscarPorCorreo(correoIngresado)
                }

                val accesoValido = if (usuario != null) {
                    withContext(Dispatchers.Default) {
                        SeguridadContrasena.verificar(
                            contrasenaIngresada,
                            usuario.contrasenaSalt,
                            usuario.contrasenaHash
                        )
                    }
                } else {
                    false
                }

                if (accesoValido && usuario != null) {
                    _usuarioId.value = usuario.id
                    _mensaje.value = "Inicio de sesión correcto"
                } else {
                    _usuarioId.value = null
                    _mensaje.value = "Correo o contraseña incorrectos"
                }

            } catch (e: Exception) {
                _mensaje.value = "No se pudo iniciar sesión"
            }
        }
    }

}
