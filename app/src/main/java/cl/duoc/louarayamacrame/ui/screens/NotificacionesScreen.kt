package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duoc.louarayamacrame.data.CampanaEntity
import cl.duoc.louarayamacrame.ui.components.CabeceraPantalla
import cl.duoc.louarayamacrame.ui.theme.CafeProfundo
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Terracota
import cl.duoc.louarayamacrame.ui.theme.TerracotaClaro

/**
 * Pantalla 14: notificaciones. Muestra las campañas publicadas como avisos dentro de la app
 * (no son notificaciones push del teléfono).
 */
@Composable
fun NotificacionesScreen(campanas: List<CampanaEntity>, volver: () -> Unit) {
    val visibles = campanas.filter { it.activa }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CabeceraPantalla("Notificaciones", volver)
        if (visibles.isEmpty()) {
            Text(
                "Todavía no hay novedades publicadas.",
                color = CafeSuave,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(visibles, key = { it.id }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TerracotaClaro),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Notifications,
                                contentDescription = null,
                                tint = Terracota
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                item.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                color = CafeProfundo
                            )
                            Text(
                                item.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = CafeSuave
                            )
                            Text(
                                "Vigencia: ${item.fecha}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CafeSuave
                            )
                        }
                    }
                }
            }
        }
    }
}
