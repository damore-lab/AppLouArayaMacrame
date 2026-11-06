package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.louarayamacrame.ui.components.BotonMacrame
import cl.duoc.louarayamacrame.ui.components.CabeceraPantalla
import cl.duoc.louarayamacrame.ui.components.CampoMacrame
import cl.duoc.louarayamacrame.ui.components.RamaDecorativa
import cl.duoc.louarayamacrame.ui.theme.ArenaClara
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Crema
import cl.duoc.louarayamacrame.ui.theme.Terracota
import cl.duoc.louarayamacrame.viewmodel.LoginViewModel

/** Pantalla 3: inicio de sesión. La lógica (ViewModel y validaciones) no cambia. */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onRegistro: () -> Unit,
    onVolver: () -> Unit,
    onLoginExitoso: () -> Unit
) {
    val correo by viewModel.correo.collectAsStateWithLifecycle()
    val contrasena by viewModel.contrasena.collectAsStateWithLifecycle()
    val errorCorreo by viewModel.errorCorreo.collectAsStateWithLifecycle()
    val errorContrasena by viewModel.errorContrasena.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()

    LaunchedEffect(mensaje) {
        if (mensaje == "Inicio de sesión correcto") {
            onLoginExitoso()
        }
    }

    var mostrarContrasena by rememberSaveable { mutableStateOf(false) }
    // Solo orienta al usuario: el rol real siempre lo define la cuenta guardada en Room.
    var accesoAdmin by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Crema, ArenaClara)))
    ) {
        RamaDecorativa(
            Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 8.dp)
                .size(width = 54.dp, height = 100.dp)
        )
        RamaDecorativa(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 8.dp)
                .size(width = 54.dp, height = 100.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CabeceraPantalla(titulo = "", onVolver = onVolver)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    "Bienvenido de nuevo",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Ingresa a tu cuenta",
                    style = MaterialTheme.typography.bodyLarge,
                    color = CafeSuave
                )

                Spacer(Modifier.height(20.dp))

                SelectorAcceso(accesoAdmin) { accesoAdmin = it }

                Spacer(Modifier.height(6.dp))
                Text(
                    if (accesoAdmin) "Usa las credenciales de administración."
                    else "Usa el correo con el que te registraste.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CafeSuave
                )

                Spacer(Modifier.height(16.dp))

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

                Spacer(Modifier.height(16.dp))

                BotonMacrame("Iniciar sesión", onClick = { viewModel.iniciarSesion() })

                if (mensaje.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(12.dp))

                TextButton(onClick = onRegistro) {
                    Text("¿No tienes cuenta? ", color = CafeSuave)
                    Text("Regístrate", color = Terracota, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(96.dp))
            }
        }
    }
}

@Composable
private fun SelectorAcceso(admin: Boolean, cambiar: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(4.dp)
    ) {
        listOf("Cliente" to false, "Administrador" to true).forEach { (texto, esAdmin) ->
            val activo = admin == esAdmin
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (activo) Terracota else Color.Transparent)
                    .clickable { cambiar(esAdmin) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    texto,
                    color = if (activo) Color.White else CafeSuave,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
