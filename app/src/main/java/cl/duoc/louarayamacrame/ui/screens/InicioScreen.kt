package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/* revisar start
        Puntos 10 y 19: incorporar una bienvenida animada breve con imágenes locales
        representativas de los diseños. Punto 11: comprobar desplazamiento y botones
        con texto grande y pantallas pequeñas, manteniendo accesibles los dos accesos.
 */
@Composable
fun InicioScreen(
    onLogin: () -> Unit,
    onRegistro: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Lou Araya Macramé",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(12.dp))

        Text("Diseños artesanales hechos con dedicación")

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRegistro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear cuenta")
        }
    }
}
//revisar end
