package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.louarayamacrame.data.UsuarioEntity
import cl.duoc.louarayamacrame.viewmodel.PerfilViewModel

/* revisar start
        Puntos 1 y 8: gestionar el borrador de nombre y alias en el ViewModel con
        restauración por usuario; no perder cambios sin guardar al recrear la actividad.
        Puntos 2, 3 y 6: observar progreso, bloquear guardados repetidos y marcar
        el error de nombre en su campo, con mensajes e íconos coherentes.
        Puntos 9 y 11: añadir regreso visible, desplazamiento con teclado y texto grande.
        Punto 15: editar avatar y preferencias de catálogo y avisos por usuario,
        y consultar el estado actual de permisos de cámara y ubicación al sistema.
 */
@Composable
fun PerfilScreen(
    usuario: UsuarioEntity,
    viewModel: PerfilViewModel,
    onGuardar: (String, String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    var editando by remember { mutableStateOf(false) }
    var nombre by remember(usuario.id, usuario.nombre) {
        mutableStateOf(usuario.nombre)
    }
    var alias by remember(usuario.id, usuario.alias) {
        mutableStateOf(usuario.alias)
    }

    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    val errorAlias by viewModel.errorAlias.collectAsStateWithLifecycle()

    LaunchedEffect(mensaje) {
        if (mensaje == "Perfil actualizado correctamente") {
            editando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        if (editando) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = alias,
                onValueChange = { alias = it },
                label = { Text("Alias de usuario") },
                singleLine = true,
                isError = errorAlias.isNotEmpty(),
                supportingText = {
                    if (errorAlias.isNotEmpty()) {
                        Text(errorAlias)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = { onGuardar(nombre, alias) }) {
                Text("Guardar cambios")
            }

            TextButton(onClick = { editando = false }) {
                Text("Cancelar")
            }
        } else {
            Text("Nombre: ${usuario.nombre}")
            Spacer(Modifier.height(8.dp))

            Text("Correo: ${usuario.correo}")
            Spacer(Modifier.height(8.dp))

            Text("Alias: ${usuario.alias}")
            Spacer(Modifier.height(8.dp))

            Text("Rol: ${usuario.rol}")

            Spacer(Modifier.height(16.dp))

            Button(onClick = { editando = true }) {
                Text("Editar perfil")
            }
        }

        if (mensaje.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = onCerrarSesion) {
            Text("Cerrar sesión")
        }
    }
}
//revisar end
