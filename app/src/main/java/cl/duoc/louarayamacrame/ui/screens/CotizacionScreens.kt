package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.louarayamacrame.data.ProductoEntity
import cl.duoc.louarayamacrame.data.SolicitudEntity
import cl.duoc.louarayamacrame.ui.components.BotonMacrame
import cl.duoc.louarayamacrame.ui.components.CabeceraPantalla
import cl.duoc.louarayamacrame.ui.components.CampoMacrame
import cl.duoc.louarayamacrame.ui.components.EtiquetaEstado
import cl.duoc.louarayamacrame.ui.components.FilaChips
import cl.duoc.louarayamacrame.ui.components.ImagenProducto
import cl.duoc.louarayamacrame.ui.components.codigoSolicitud
import cl.duoc.louarayamacrame.ui.theme.CafeProfundo
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Oliva
import cl.duoc.louarayamacrame.ui.theme.Terracota
import cl.duoc.louarayamacrame.validation.ValidacionesComercio
import kotlinx.coroutines.delay

private val paletaColores = listOf(
    "Crudo" to Color(0xFFEBDCC8),
    "Arena" to Color(0xFFD9BC9A),
    "Terracota" to Color(0xFFA45A40),
    "Cobre" to Color(0xFFC27B4F),
    "Marfil" to Color(0xFFF7EFE4)
)

/**
 * Pantalla 10: solicitud de cotización.
 * La solicitud se guarda de inmediato con [guardar]; la animación de confirmación se muestra
 * un instante y luego se llama a [alTerminar] para pasar al historial.
 */
@Composable
fun CotizacionScreen(
    usuarioId: Int,
    productoId: Int?,
    guardar: (SolicitudEntity) -> Unit,
    volver: () -> Unit,
    alTerminar: () -> Unit = {}
) {
    var evento by remember { mutableStateOf("") }
    var medidas by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var colores by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var validar by remember { mutableStateOf(false) }
    var enviada by remember { mutableStateOf(false) }

    val obligatorio = "Este campo es obligatorio"
    fun falta(valor: String) = if (validar && valor.isBlank()) obligatorio else ""

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(Modifier.fillMaxSize()) {
            CabeceraPantalla("Solicitud de personalización", volver)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Text("Tipo de evento", style = MaterialTheme.typography.titleMedium, color = CafeProfundo)
                Spacer(Modifier.height(6.dp))
                FilaChips(
                    opciones = listOf("Matrimonio", "Festival", "Hogar", "Corporativo"),
                    seleccion = evento,
                    onSeleccion = { evento = it }
                )
                Spacer(Modifier.height(6.dp))
                CampoMacrame(evento, { evento = it }, "Tipo de evento", error = falta(evento))

                CampoMacrame(
                    medidas, { medidas = it }, "Dimensiones aproximadas",
                    placeholder = "Ej: 3 x 2 metros", error = falta(medidas)
                )

                Text("Material preferido", style = MaterialTheme.typography.titleMedium, color = CafeProfundo)
                Spacer(Modifier.height(6.dp))
                FilaChips(
                    opciones = listOf("Algodón natural", "Yute", "Lana", "Cuerda de algodón"),
                    seleccion = material,
                    onSeleccion = { material = it }
                )
                Spacer(Modifier.height(6.dp))
                CampoMacrame(material, { material = it }, "Material preferido", error = falta(material))

                Text("Color o paleta de colores", style = MaterialTheme.typography.titleMedium, color = Terracota)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    paletaColores.forEach { (nombre, color) ->
                        val elegido = colores.split(",").map { it.trim() }.contains(nombre)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (elegido) 3.dp else 1.dp,
                                    color = if (elegido) Terracota else MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                )
                                .clickable {
                                    val actuales = colores.split(",").map { it.trim() }
                                        .filter { it.isNotEmpty() }
                                    colores = if (elegido) actuales.filter { it != nombre }.joinToString(", ")
                                    else (actuales + nombre).joinToString(", ")
                                }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                CampoMacrame(
                    colores, { colores = it }, "Colores",
                    placeholder = "Toca los colores o escríbelos", error = falta(colores)
                )

                val errorFecha = if (validar && !ValidacionesComercio.fechaValida(fecha)) {
                    if (fecha.isBlank()) obligatorio
                    else "Ingresa una fecha válida, desde hoy, en formato DD/MM/AAAA"
                } else ""
                CampoMacrame(
                    fecha, { fecha = it }, "Fecha requerida (DD/MM/AAAA)",
                    icono = Icons.Filled.DateRange, error = errorFecha
                )

                CampoMacrame(
                    observaciones, { observaciones = it }, "Observaciones",
                    singleLine = false, minLineas = 3,
                    placeholder = "Cuéntanos tu idea o referencia…"
                )

                Spacer(Modifier.height(8.dp))
            }
            Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                BotonMacrame(
                    "Enviar solicitud",
                    habilitado = !enviada,
                    onClick = {
                        validar = true
                        if (ValidacionesComercio.cotizacionValida(evento, medidas, material, colores, fecha)) {
                            enviada = true
                            guardar(
                                SolicitudEntity(
                                    usuarioId = usuarioId,
                                    productoId = productoId,
                                    evento = evento.trim(),
                                    medidas = medidas.trim(),
                                    material = material.trim(),
                                    colores = colores.trim(),
                                    fecha = fecha.trim(),
                                    observaciones = observaciones.trim()
                                )
                            )
                        }
                    }
                )
            }
        }
        if (enviada) ConfirmacionEnvio(alTerminar)
    }
}

/** Animación de confirmación: un círculo con visto que crece y luego pasa al historial. */
@Composable
private fun ConfirmacionEnvio(alTerminar: () -> Unit) {
    val escala = remember { Animatable(0.3f) }
    LaunchedEffect(Unit) {
        escala.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        delay(1100)
        alTerminar()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .scale(escala.value)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Oliva),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                "¡Solicitud enviada!",
                style = MaterialTheme.typography.headlineSmall,
                color = CafeProfundo
            )
            Text(
                "Puedes revisar su estado en tus solicitudes.",
                color = CafeSuave,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 6.dp)
            )
        }
    }
}

/** Pantalla 13: historial del cliente o gestión de estados para la administradora. */
@Composable
fun SolicitudesScreen(
    items: List<SolicitudEntity>,
    admin: Boolean,
    cambiar: (Int, String) -> Unit,
    volver: () -> Unit,
    productos: List<ProductoEntity> = emptyList()
) {
    var filtro by remember { mutableStateOf("Todas") }
    val lista = items.filter { filtro == "Todas" || it.estado == filtro }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CabeceraPantalla(if (admin) "Gestión de solicitudes" else "Mis solicitudes", volver)
        Column(Modifier.padding(horizontal = 16.dp)) {
            FilaChips(
                opciones = listOf("Todas", "Pendiente", "Confirmado", "Entregado"),
                seleccion = filtro,
                onSeleccion = { filtro = it }
            )
            Spacer(Modifier.height(10.dp))
        }
        if (lista.isEmpty()) {
            Text(
                if (items.isEmpty()) "Aún no hay solicitudes registradas."
                else "No hay solicitudes con este estado.",
                color = CafeSuave,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(lista, key = { it.id }) { solicitud ->
                val producto = productos.find { it.id == solicitud.productoId }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ImagenProducto(
                                producto?.imagenUri,
                                Modifier.size(64.dp),
                                semilla = solicitud.productoId ?: solicitud.id
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        codigoSolicitud(solicitud.id),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = CafeSuave,
                                        modifier = Modifier.weight(1f)
                                    )
                                    EtiquetaEstado(solicitud.estado)
                                }
                                Text(
                                    producto?.nombre ?: solicitud.evento,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = CafeProfundo,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Para el ${solicitud.fecha}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CafeSuave
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "${solicitud.evento} · ${solicitud.medidas}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CafeProfundo
                        )
                        Text(
                            "Material: ${solicitud.material} · ${solicitud.colores}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CafeSuave
                        )
                        if (solicitud.observaciones.isNotBlank()) {
                            Text(
                                solicitud.observaciones,
                                style = MaterialTheme.typography.bodySmall,
                                color = CafeSuave
                            )
                        }
                        if (admin) {
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("Pendiente", "Confirmado", "Entregado").forEach { estado ->
                                    val activo = solicitud.estado == estado
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                if (activo) Terracota
                                                else MaterialTheme.colorScheme.surfaceContainerHigh
                                            )
                                            .clickable { cambiar(solicitud.id, estado) }
                                            .padding(horizontal = 12.dp, vertical = 7.dp)
                                    ) {
                                        Text(
                                            estado,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (activo) Color.White else CafeSuave,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
