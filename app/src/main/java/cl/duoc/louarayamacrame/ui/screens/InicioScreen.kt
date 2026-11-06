package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cl.duoc.louarayamacrame.ui.components.BotonMacrame
import cl.duoc.louarayamacrame.ui.components.LogoMacrame
import cl.duoc.louarayamacrame.ui.components.RamaDecorativa
import cl.duoc.louarayamacrame.ui.theme.ArenaClara
import cl.duoc.louarayamacrame.ui.theme.CafeProfundo
import cl.duoc.louarayamacrame.ui.theme.Crema
import cl.duoc.louarayamacrame.ui.theme.Terracota
import cl.duoc.louarayamacrame.ui.theme.TerracotaClaro

/** Pantalla 1: bienvenida con animación de entrada y los dos accesos (ingresar o crear cuenta). */
@Composable
fun InicioScreen(
    onLogin: () -> Unit,
    onRegistro: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Crema, ArenaClara)))
    ) {
        RamaDecorativa(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 24.dp, end = 12.dp)
                .size(width = 60.dp, height = 110.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 6 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LogoMacrame(tamanoRama = 96.dp)
                    Spacer(Modifier.height(28.dp))
                    PuntoBienvenida(Icons.Filled.Star, "Diseños artesanales únicos")
                    PuntoBienvenida(Icons.Filled.Home, "Para hogares, eventos y más")
                    PuntoBienvenida(Icons.Filled.LocationOn, "Encuentra en ferias y showrooms")
                    PuntoBienvenida(Icons.Filled.Create, "Solicita diseños personalizados")
                }
            }
            Spacer(Modifier.height(32.dp))
            BotonMacrame("Comenzar", onLogin)
            Spacer(Modifier.height(12.dp))
            BotonMacrame("Crear cuenta", onRegistro, secundario = true)
        }
    }
}

@Composable
private fun PuntoBienvenida(icono: ImageVector, texto: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(TerracotaClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = null, tint = Terracota, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text(texto, style = MaterialTheme.typography.bodyLarge, color = CafeProfundo)
    }
}
