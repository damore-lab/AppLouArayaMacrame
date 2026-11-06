package cl.duoc.louarayamacrame.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.louarayamacrame.data.UsuarioEntity
import cl.duoc.louarayamacrame.ui.components.AvatarIniciales
import cl.duoc.louarayamacrame.ui.components.BotonMacrame
import cl.duoc.louarayamacrame.ui.components.CabeceraPantalla
import cl.duoc.louarayamacrame.ui.components.CampoMacrame
import cl.duoc.louarayamacrame.ui.theme.CafeProfundo
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Terracota
import cl.duoc.louarayamacrame.viewmodel.PerfilViewModel

/** Pantalla 15: perfil y configuración. Edición, guardado y cierre de sesión no cambian. */
@Composable
fun PerfilScreen(
    usuario: UsuarioEntity,
    viewModel: PerfilViewModel,
    onGuardar: (String, String) -> Unit,
    onCerrarSesion: () -> Unit,
    onVolver: () -> Unit = {}
) {
    val context = LocalContext.current
    var editando by remember { mutableStateOf(false) }
    var nombre by remember(usuario.id, usuario.nombre) { mutableStateOf(usuario.nombre) }
    var alias by remember(usuario.id, usuario.alias) { mutableStateOf(usuario.alias) }

    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    val errorAlias by viewModel.errorAlias.collectAsStateWithLifecycle()

    LaunchedEffect(mensaje) {
        if (mensaje == "Perfil actualizado correctamente") {
            editando = false
        }
    }

    val permisoUbicacion = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val admin = usuario.rol == "ADMIN"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CabeceraPantalla("Mi perfil", onVolver)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarIniciales(usuario.nombre, tamano = 72.dp)
                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = 14.dp)
                ) {
                    Text(
                        usuario.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        color = CafeProfundo
                    )
                    Text(usuario.correo, color = CafeSuave)
                }
                if (!editando) {
                    IconButton(onClick = { editando = true }) {
                        Icon(
                            Icons.Filled.Create,
                            contentDescription = "Editar perfil",
                            tint = Terracota
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            if (editando) {
                CampoMacrame(
                    nombre, { nombre = it }, "Nombre completo",
                    icono = Icons.Filled.Person
                )
                CampoMacrame(
                    alias, { alias = it }, "Alias de usuario",
                    icono = Icons.Filled.AccountCircle,
                    error = errorAlias
                )
                Spacer(Modifier.height(8.dp))
                BotonMacrame("Guardar cambios", onClick = { onGuardar(nombre, alias) })
                TextButton(
                    onClick = { editando = false },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) { Text("Cancelar", color = CafeSuave) }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(horizontal = 14.dp)
                ) {
                    FilaPerfil("Nombre", usuario.nombre)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaPerfil("Alias", usuario.alias)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaPerfil("Correo", usuario.correo)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaPerfil("Rol", if (admin) "Administrador" else "Cliente")
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaPerfil(
                        "Permiso de ubicación",
                        if (permisoUbicacion) "Concedido" else "Se pedirá al usarlo"
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaPerfil("Cámara", "App de cámara del teléfono")
                }
                Spacer(Modifier.height(12.dp))
                BotonMacrame("Editar perfil", onClick = { editando = true }, secundario = true)
            }

            if (mensaje.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text(mensaje, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(20.dp))
            BotonMacrame(
                "Cerrar sesión",
                onClick = onCerrarSesion,
                secundario = true,
                peligro = true
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FilaPerfil(titulo: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(titulo, color = CafeProfundo, fontWeight = FontWeight.SemiBold)
        Text(valor, color = CafeSuave)
    }
}
