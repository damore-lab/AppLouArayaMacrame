package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.louarayamacrame.viewmodel.LoginViewModel

/* revisar start
        Puntos 2 y 3: observar un estado de autenticación en curso y deshabilitar
        Ingresar hasta obtener resultado, conservando los campos cuando haya error.
        Puntos 8 y 9: reaccionar a un resultado de autenticación explícito,
        sin usar el texto del mensaje como señal para navegar.
        Punto 11: permitir desplazamiento con teclado y texto grande y usar
        íconos con etiquetas accesibles; comprobarlo en pantalla pequeña.
 */
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

    var mostrarContrasena by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = viewModel::actualizarCorreo,
            label = { Text("Correo electrónico") },
            leadingIcon = { Text("✉") },
            singleLine = true,
            isError = errorCorreo.isNotEmpty(),
            supportingText = {
                if (errorCorreo.isNotEmpty()) {
                    Text(errorCorreo)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = viewModel::actualizarContrasena,
            label = { Text("Contraseña") },
            leadingIcon = { Text("🔒") },
            singleLine = true,
            isError = errorContrasena.isNotEmpty(),
            supportingText = {
                if (errorContrasena.isNotEmpty()) {
                    Text(errorContrasena)
                }
            },
            visualTransformation = if (mostrarContrasena) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                TextButton(
                    onClick = {
                        mostrarContrasena = !mostrarContrasena
                    }
                ) {
                    Text(
                        if (mostrarContrasena) "Ocultar"
                        else "Ver"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.iniciarSesion()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        if (mensaje.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onRegistro) {
            Text("¿No tienes cuenta? Regístrate")
        }

        TextButton(onClick = onVolver) {
            Text("Volver")
        }
    }
}
//revisar end
