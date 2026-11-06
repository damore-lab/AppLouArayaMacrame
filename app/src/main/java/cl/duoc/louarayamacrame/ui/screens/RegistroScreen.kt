package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.louarayamacrame.ui.components.BotonMacrame
import cl.duoc.louarayamacrame.ui.components.CabeceraPantalla
import cl.duoc.louarayamacrame.ui.components.CampoMacrame
import cl.duoc.louarayamacrame.ui.theme.ArenaClara
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Crema
import cl.duoc.louarayamacrame.ui.theme.Terracota
import cl.duoc.louarayamacrame.viewmodel.RegistroViewModel

/** Pantalla 2: registro de cliente. La lógica (ViewModel y validaciones) no cambia. */
@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onVolver: () -> Unit
) {
    val nombre by viewModel.nombre.collectAsStateWithLifecycle()
    val alias by viewModel.alias.collectAsStateWithLifecycle()
    val errorAlias by viewModel.errorAlias.collectAsStateWithLifecycle()
    val correo by viewModel.correo.collectAsStateWithLifecycle()
    val contrasena by viewModel.contrasena.collectAsStateWithLifecycle()
    val confirmacion by viewModel.confirmacion.collectAsStateWithLifecycle()

    val errorNombre by viewModel.errorNombre.collectAsStateWithLifecycle()
    val errorCorreo by viewModel.errorCorreo.collectAsStateWithLifecycle()
    val errorContrasena by viewModel.errorContrasena.collectAsStateWithLifecycle()
    val errorConfirmacion by viewModel.errorConfirmacion.collectAsStateWithLifecycle()

    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()

    var mostrarContrasena by rememberSaveable { mutableStateOf(false) }
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Crema, ArenaClara)))
            .verticalScroll(rememberScrollState())
    ) {
        CabeceraPantalla(titulo = "", onVolver = onVolver)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Únete a Lou Araya Macramé y sé parte de nuestra comunidad",
                style = MaterialTheme.typography.bodyLarge,
                color = CafeSuave
            )

            Spacer(Modifier.height(20.dp))

            CampoMacrame(
                valor = nombre,
                onCambio = viewModel::actualizarNombre,
                etiqueta = "Nombre completo",
                icono = Icons.Filled.Person,
                error = errorNombre
            )
            Spacer(Modifier.height(8.dp))

            CampoMacrame(
                valor = alias,
                onCambio = viewModel::actualizarAlias,
                etiqueta = "Alias de usuario",
                icono = Icons.Filled.AccountCircle,
                error = errorAlias
            )
            Spacer(Modifier.height(8.dp))

            CampoMacrame(
                valor = correo,
                onCambio = viewModel::actualizarCorreo,
                etiqueta = "Correo electrónico",
                icono = Icons.Filled.Email,
                error = errorCorreo,
                teclado = KeyboardType.Email
            )
            Spacer(Modifier.height(8.dp))

            CampoMacrame(
                valor = contrasena,
                onCambio = viewModel::actualizarContrasena,
                etiqueta = "Contraseña",
                icono = Icons.Filled.Lock,
                error = errorContrasena,
                teclado = KeyboardType.Password,
                transformacion = if (mostrarContrasena) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailing = {
                    TextButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                        Text(if (mostrarContrasena) "Ocultar" else "Ver", color = Terracota)
                    }
                }
            )
            Spacer(Modifier.height(8.dp))

            CampoMacrame(
                valor = confirmacion,
                onCambio = viewModel::actualizarConfirmacion,
                etiqueta = "Confirmar contraseña",
                icono = Icons.Filled.Lock,
                error = errorConfirmacion,
                teclado = KeyboardType.Password,
                transformacion = if (mostrarConfirmacion) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailing = {
                    TextButton(onClick = { mostrarConfirmacion = !mostrarConfirmacion }) {
                        Text(if (mostrarConfirmacion) "Ocultar" else "Ver", color = Terracota)
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            BotonMacrame("Crear cuenta", onClick = { viewModel.registrarUsuario() })

            if (mensaje.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = onVolver,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = Terracota)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
