package cl.duoc.louarayamacrame.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.louarayamacrame.validation.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import cl.duoc.louarayamacrame.data.UsuarioDao
import cl.duoc.louarayamacrame.data.UsuarioEntity
import cl.duoc.louarayamacrame.data.SeguridadContrasena
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/* revisar start
        Puntos 2 y 3: publicar progreso y resultado del registro y evitar inserciones
        simultáneas. Tomar una copia coherente de los campos antes del trabajo asíncrono.
        Puntos 8 y 15: usar repositorio local, centralizar las reglas compartidas
        de alias y gestionar el avatar seleccionado junto con los datos del perfil.
 */
class RegistroViewModel : ViewModel() {

    private var usuarioDao: UsuarioDao? = null

    fun configurarDao(dao: UsuarioDao) {
        usuarioDao = dao
    }

    private val _nombre = MutableStateFlow("")
    val nombre = _nombre.asStateFlow()
    private val _alias = MutableStateFlow("")
    val alias = _alias.asStateFlow()

    private val _errorAlias = MutableStateFlow("")
    val errorAlias = _errorAlias.asStateFlow()

    fun actualizarAlias(valor: String) {
        _alias.value = valor
        _errorAlias.value = ""
        _mensaje.value = ""
    }

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
        _errorAlias.value = ""
        _errorContrasena.value = ""
        _errorConfirmacion.value = ""
        _mensaje.value = ""

        val nombreIngresado = _nombre.value.trim()
        val correoIngresado = _correo.value.trim()

        if (!Validaciones.nombreValido(nombreIngresado)) {
            _errorNombre.value = "El nombre es obligatorio"
        }

        if (correoIngresado.isEmpty()) {
            _errorCorreo.value = "El correo es obligatorio"
        } else if (!Validaciones.correoValido(correoIngresado)) {
            _errorCorreo.value = "Ingresa un correo válido"
        }

        if (_contrasena.value.isEmpty()) {
            _errorContrasena.value = "La contraseña es obligatoria"
        } else if (!Validaciones.contrasenaRegistroValida(_contrasena.value)) {
            _errorContrasena.value =
                "La contraseña debe tener al menos 8 caracteres"
        }

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

        if (_alias.value.trim().length < 3) {
            _errorAlias.value = "El alias debe tener al menos 3 caracteres"
        }

        val esValido = _errorNombre.value.isEmpty() &&
                _errorAlias.value.isEmpty() &&
                _errorCorreo.value.isEmpty() &&
                _errorContrasena.value.isEmpty() &&
                _errorConfirmacion.value.isEmpty()

        if (esValido) {
            _mensaje.value =
                "Formulario válido. Registro pendiente."
        }

        return esValido
    }

    fun registrarUsuario() {
        if (!validarFormulario()) return

        val dao = usuarioDao ?: run {
            _mensaje.value = "No se pudo acceder a la base de datos"
            return
        }

        viewModelScope.launch {
            try {
                val correoNormalizado = _correo.value.trim().lowercase()

                val existente = withContext(Dispatchers.IO) {
                    dao.buscarPorCorreo(correoNormalizado)
                }

                if (existente != null) {
                    _errorCorreo.value = "Este correo ya está registrado"
                    _mensaje.value = ""
                    return@launch
                }

                val aliasIngresado = _alias.value.trim()

                val aliasExistente = withContext(Dispatchers.IO) {
                    dao.buscarPorAlias(aliasIngresado)
                }

                if (aliasExistente != null) {
                    _errorAlias.value = "Este alias ya está registrado"
                    _mensaje.value = ""
                    return@launch
                }

                val salt = withContext(Dispatchers.Default) {
                    SeguridadContrasena.generarSalt()
                }

                val hash = withContext(Dispatchers.Default) {
                    SeguridadContrasena.generarHash(_contrasena.value, salt)
                }

                val usuario = UsuarioEntity(
                    nombre = _nombre.value.trim(),
                    alias = _alias.value.trim(),
                    correo = correoNormalizado,
                    contrasenaHash = hash,
                    contrasenaSalt = salt,
                    rol = "CLIENTE"
                )

                withContext(Dispatchers.IO) {
                    dao.insertarUsuario(usuario)
                }

                _mensaje.value = "Usuario registrado correctamente"

            } catch (e: Exception) {
                _mensaje.value = "No se pudo completar el registro"
            }
        }
    }
}
//revisar end
