package cl.duoc.louarayamacrame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.louarayamacrame.data.UsuarioDao
import cl.duoc.louarayamacrame.data.UsuarioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilViewModel : ViewModel() {

    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    private val _errorAlias = MutableStateFlow("")
    val errorAlias = _errorAlias.asStateFlow()

    fun actualizarPerfil(
        usuario: UsuarioEntity,
        nombreNuevo: String,
        aliasNuevo: String,
        dao: UsuarioDao,
        onGuardado: (UsuarioEntity) -> Unit
    ) {
        _mensaje.value = ""
        _errorAlias.value = ""

        val nombre = nombreNuevo.trim()
        val alias = aliasNuevo.trim()

        if (nombre.isEmpty()) {
            _mensaje.value = "El nombre es obligatorio"
            return
        }

        if (alias.length < 3) {
            _errorAlias.value = "El alias debe tener al menos 3 caracteres"
            return
        }

        viewModelScope.launch {
            try {
                val existente = withContext(Dispatchers.IO) {
                    dao.buscarPorAlias(alias)
                }

                if (existente != null && existente.id != usuario.id) {
                    _errorAlias.value = "Este alias ya está registrado"
                    return@launch
                }

                val usuarioActualizado = usuario.copy(
                    nombre = nombre,
                    alias = alias
                )

                val filasActualizadas = withContext(Dispatchers.IO) {
                    dao.actualizarUsuario(usuarioActualizado)
                }

                if (filasActualizadas > 0) {
                    _mensaje.value = "Perfil actualizado correctamente"
                    onGuardado(usuarioActualizado)
                } else {
                    _mensaje.value = "No se pudo actualizar el perfil"
                }

            } catch (e: Exception) {
                _mensaje.value = "No se pudo actualizar el perfil"
            }
        }
    }
}
