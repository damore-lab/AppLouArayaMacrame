package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.louarayamacrame.data.CampanaEntity
import cl.duoc.louarayamacrame.data.ProductoEntity
import cl.duoc.louarayamacrame.ui.components.AvatarIniciales
import cl.duoc.louarayamacrame.ui.components.BotonMacrame
import cl.duoc.louarayamacrame.ui.components.ImagenProducto
import cl.duoc.louarayamacrame.ui.components.formatoPrecio
import cl.duoc.louarayamacrame.ui.theme.CafeProfundo
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Terracota
import cl.duoc.louarayamacrame.ui.theme.TerracotaClaro
import cl.duoc.louarayamacrame.ui.theme.TerracotaOscuro

private class Acceso(val clave: String, val etiqueta: String, val icono: ImageVector)

/** Pantalla 4: principal. Los accesos conservan los mismos textos de navegación de siempre. */
@Composable
fun DashboardScreen(
    nombre: String,
    admin: Boolean,
    abrir: (String) -> Unit,
    campanas: List<CampanaEntity> = emptyList(),
    productos: List<ProductoEntity> = emptyList(),
    abrirProducto: (Int) -> Unit = {}
) {
    var mostrar by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { mostrar = true }

    val accesos = if (admin) listOf(
        Acceso("Catálogo", "Catálogo", Icons.AutoMirrored.Filled.List),
        Acceso("Productos", "Productos", Icons.Filled.ShoppingCart),
        Acceso("Campañas", "Campañas", Icons.Filled.Star),
        Acceso("Solicitudes", "Solicitudes", Icons.Filled.DateRange),
        Acceso("Ferias y mapa", "Mapa", Icons.Filled.LocationOn),
        Acceso("Escanear QR", "Escáner QR", Icons.Filled.Search),
        Acceso("Notificaciones", "Avisos", Icons.Filled.Notifications),
        Acceso("Perfil", "Perfil", Icons.Filled.Person)
    ) else listOf(
        Acceso("Catálogo", "Catálogo", Icons.AutoMirrored.Filled.List),
        Acceso("Mis favoritos", "Favoritos", Icons.Filled.Favorite),
        Acceso("Solicitar cotización", "Cotizar", Icons.Filled.Create),
        Acceso("Ferias y mapa", "Mapa", Icons.Filled.LocationOn),
        Acceso("Escanear QR", "Escáner QR", Icons.Filled.Search),
        Acceso("Mis solicitudes", "Solicitudes", Icons.Filled.DateRange),
        Acceso("Notificaciones", "Avisos", Icons.Filled.Notifications),
        Acceso("Perfil", "Perfil", Icons.Filled.Person)
    )
    val destacada = campanas.firstOrNull { it.activa }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Saludo y campana de avisos.
        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarIniciales(nombre, tamano = 48.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "Hola, $nombre",
                    style = MaterialTheme.typography.titleLarge,
                    color = CafeProfundo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    if (admin) "Panel de administración" else "Qué bueno verte por aquí",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CafeSuave
                )
            }
            IconButton(onClick = { abrir("Notificaciones") }) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Terracota
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Buscador: abre el catálogo, que es donde se realiza la búsqueda.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .clickable { abrir("Catálogo") }
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = CafeSuave)
            Spacer(Modifier.width(10.dp))
            Text("Buscar productos, ferias…", color = CafeSuave)
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = mostrar,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 })
        ) {
            TarjetaDestacada(
                titulo = destacada?.titulo ?: "Diseños artesanales únicos",
                detalle = destacada?.descripcion ?: "Piezas de macramé hechas a mano para tu gran día",
                etiqueta = if (destacada != null) "Novedad" else "Colección",
                onClick = { abrir("Catálogo") }
            )
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "Accesos rápidos",
            style = MaterialTheme.typography.titleMedium,
            color = CafeProfundo
        )
        Spacer(Modifier.height(10.dp))
        accesos.chunked(4).forEach { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                fila.forEach { acceso ->
                    AccesoRapido(acceso, Modifier.weight(1f)) { abrir(acceso.clave) }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        if (productos.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Productos destacados",
                    style = MaterialTheme.typography.titleMedium,
                    color = CafeProfundo,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = { abrir("Catálogo") }) {
                    Text("Ver todos", color = Terracota)
                }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(productos.take(6), key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .width(150.dp)
                            .clickable { abrirProducto(item.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        ImagenProducto(
                            item.imagenUri,
                            Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            semilla = item.id
                        )
                        Column(Modifier.padding(10.dp)) {
                            Text(
                                item.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                formatoPrecio(item.precio),
                                color = Terracota,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun TarjetaDestacada(titulo: String, detalle: String, etiqueta: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.horizontalGradient(listOf(TerracotaOscuro, Terracota)))
    ) {
        ImagenProducto(
            null,
            Modifier
                .align(Alignment.CenterEnd)
                .width(130.dp)
                .height(170.dp),
            semilla = 0
        )
        Column(
            Modifier
                .padding(18.dp)
                .fillMaxWidth(0.62f)
        ) {
            Text(etiqueta, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.labelLarge)
            Text(
                titulo,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                detalle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(12.dp))
            BotonMacrame(
                "Ver colección",
                onClick,
                modifier = Modifier.width(150.dp)
            )
        }
    }
}

@Composable
private fun AccesoRapido(acceso: Acceso, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(TerracotaClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(acceso.icono, contentDescription = null, tint = Terracota)
        }
        Spacer(Modifier.height(4.dp))
        Text(
            acceso.etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = CafeProfundo,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
