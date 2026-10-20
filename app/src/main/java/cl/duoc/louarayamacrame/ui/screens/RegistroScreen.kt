package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.louarayamacrame.viewmodel.RegistroViewModel

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

    var mostrarContrasena by rememberSaveable {
        mutableStateOf(false)
    }

    var mostrarConfirmacion by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Crear cuenta",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = viewModel::actualizarNombre,
            label = { Text("Nombre completo") },
            leadingIcon = { Text("♙") },
            singleLine = true,
            isError = errorNombre.isNotEmpty(),
            supportingText = {
                if (errorNombre.isNotEmpty()) {
                    Text(errorNombre)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))


        OutlinedTextField(
            value = alias,
            onValueChange = viewModel::actualizarAlias,
            label = { Text("Alias de usuario") },
            leadingIcon = { Text("@") },
            singleLine = true,
            isError = errorAlias.isNotEmpty(),
            supportingText = {
                if (errorAlias.isNotEmpty()) {
                    Text(errorAlias)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))


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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
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

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmacion,
            onValueChange = viewModel::actualizarConfirmacion,
            label = { Text("Confirmar contraseña") },
            leadingIcon = { Text("🔒") },
            singleLine = true,
            isError = errorConfirmacion.isNotEmpty(),
            supportingText = {
                if (errorConfirmacion.isNotEmpty()) {
                    Text(errorConfirmacion)
                }
            },
            visualTransformation = if (mostrarConfirmacion) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = !mostrarConfirmacion
                    }
                ) {
                    Text(
                        if (mostrarConfirmacion) "Ocultar"
                        else "Ver"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.registrarUsuario()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Registrarme",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (mensaje.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onVolver) {
            Text(
                text = "Volver",
                fontSize = 16.sp
            )
        }
    }
}
