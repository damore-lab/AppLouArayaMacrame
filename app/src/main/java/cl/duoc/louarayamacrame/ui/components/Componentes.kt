package cl.duoc.louarayamacrame.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cl.duoc.louarayamacrame.ui.theme.Ambar
import cl.duoc.louarayamacrame.ui.theme.ArenaClara
import cl.duoc.louarayamacrame.ui.theme.AzulEstado
import cl.duoc.louarayamacrame.ui.theme.CafeProfundo
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Oliva
import cl.duoc.louarayamacrame.ui.theme.Terracota
import cl.duoc.louarayamacrame.ui.theme.TerracotaClaro

/** Rutas que muestran la barra de navegación inferior compartida. */
val rutasConBarra = setOf(
    "principal", "catalogo", "favoritos", "mapa", "solicitudes",
    "productos_admin", "campanas_admin", "perfil"
)

private class DestinoBarra(val ruta: String, val etiqueta: String, val icono: ImageVector)

/** Barra inferior compartida: marca como seleccionado el destino actual y respeta el rol. */
@Composable
fun BarraInferior(rutaActual: String?, admin: Boolean, ir: (String) -> Unit) {
    val destinos = if (admin) listOf(
        DestinoBarra("principal", "Inicio", Icons.Filled.Home),
        DestinoBarra("productos_admin", "Productos", Icons.Filled.ShoppingCart),
        DestinoBarra("campanas_admin", "Campañas", Icons.Filled.Star),
        DestinoBarra("solicitudes", "Solicitudes", Icons.Filled.DateRange),
        DestinoBarra("perfil", "Perfil", Icons.Filled.Person)
    ) else listOf(
        DestinoBarra("principal", "Inicio", Icons.Filled.Home),
        DestinoBarra("catalogo", "Catálogo", Icons.AutoMirrored.Filled.List),
        DestinoBarra("mapa", "Mapa", Icons.Filled.LocationOn),
        DestinoBarra("solicitudes", "Solicitudes", Icons.Filled.DateRange),
        DestinoBarra("perfil", "Perfil", Icons.Filled.Person)
    )
    val seleccionada = if (rutaActual == "favoritos") "catalogo" else rutaActual
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        tonalElevation = 0.dp
    ) {
        destinos.forEach { destino ->
            NavigationBarItem(
                selected = seleccionada == destino.ruta,
                onClick = { ir(destino.ruta) },
                icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                label = { Text(destino.etiqueta, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Terracota,
                    selectedTextColor = Terracota,
                    indicatorColor = TerracotaClaro,
                    unselectedIconColor = CafeSuave,
                    unselectedTextColor = CafeSuave
                )
            )
        }
    }
}

/** Cabecera con botón de regreso visible, título en serif y acciones opcionales a la derecha. */
@Composable
fun CabeceraPantalla(
    titulo: String,
    onVolver: (() -> Unit)?,
    modifier: Modifier = Modifier,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onVolver != null) {
            IconButton(onClick = onVolver) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            Spacer(Modifier.width(16.dp))
        }
        Text(
            titulo,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        acciones()
    }
}

/** Campo de texto con bordes redondeados, ícono inicial y mensaje de error bajo el campo. */
@Composable
fun CampoMacrame(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    error: String = "",
    singleLine: Boolean = true,
    minLineas: Int = 1,
    teclado: KeyboardType = KeyboardType.Text,
    transformacion: VisualTransformation = VisualTransformation.None,
    placeholder: String? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        placeholder = if (placeholder != null) ({ Text(placeholder) }) else null,
        leadingIcon = if (icono != null) ({ Icon(icono, contentDescription = null) }) else null,
        trailingIcon = trailing,
        singleLine = singleLine,
        minLines = minLineas,
        isError = error.isNotEmpty(),
        supportingText = if (error.isNotEmpty()) ({ Text(error) }) else null,
        visualTransformation = transformacion,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            focusedBorderColor = Terracota,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedLabelColor = Terracota
        ),
        modifier = modifier.fillMaxWidth()
    )
}

/** Botón principal terracota (o variante con borde) a todo el ancho. */
@Composable
fun BotonMacrame(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    secundario: Boolean = false,
    habilitado: Boolean = true,
    peligro: Boolean = false
) {
    val forma = RoundedCornerShape(28.dp)
    val alto = Modifier.heightIn(min = 52.dp)
    if (secundario) {
        OutlinedButton(
            onClick = onClick,
            enabled = habilitado,
            shape = forma,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (peligro) MaterialTheme.colorScheme.error else Terracota
            ),
            modifier = modifier
                .fillMaxWidth()
                .then(alto)
        ) { Text(texto, fontWeight = FontWeight.SemiBold) }
    } else {
        Button(
            onClick = onClick,
            enabled = habilitado,
            shape = forma,
            colors = ButtonDefaults.buttonColors(
                containerColor = Terracota,
                contentColor = Color.White
            ),
            modifier = modifier
                .fillMaxWidth()
                .then(alto)
        ) { Text(texto, fontWeight = FontWeight.SemiBold) }
    }
}

/** Fila desplazable de chips de filtro (Todos, Matrimonios, ...). */
@Composable
fun FilaChips(opciones: List<String>, seleccion: String, onSeleccion: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        opciones.forEach { opcion ->
            FilterChip(
                selected = seleccion == opcion,
                onClick = { onSeleccion(opcion) },
                label = { Text(opcion, style = MaterialTheme.typography.labelLarge) },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Terracota,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    labelColor = CafeSuave
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = seleccion == opcion,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = Terracota
                )
            )
        }
    }
}

/** Etiqueta de color para estados (Pendiente, Confirmado, Entregado, Disponible...). */
@Composable
fun EtiquetaEstado(texto: String, modifier: Modifier = Modifier) {
    val color = when (texto) {
        "Confirmado", "Disponible", "Publicada", "Activa" -> Oliva
        "Pendiente", "A pedido" -> Ambar
        "Entregado", "Exploración" -> AzulEstado
        else -> CafeSuave
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/** Fotografía del producto; si no existe, dibuja una pieza de macramé como ilustración. */
@Composable
fun ImagenProducto(ruta: String?, modifier: Modifier = Modifier, semilla: Int = 0) {
    val bitmap = remember(ruta) {
        try {
            ruta?.let { BitmapFactory.decodeFile(it) }
        } catch (_: Exception) {
            null
        }
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Fotografía del producto",
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(MaterialTheme.shapes.medium)
        )
    } else {
        IlustracionMacrame(semilla, modifier.clip(MaterialTheme.shapes.medium))
    }
}

/** Ilustración dibujada con Canvas: tres variantes de piezas colgantes de macramé. */
@Composable
fun IlustracionMacrame(semilla: Int, modifier: Modifier = Modifier) {
    val fondos = listOf(Color(0xFFF1D9C4), Color(0xFFE9D3BF), Color(0xFFDDC6AE), Color(0xFFF5E2D0))
    val fondo = fondos[Math.floorMod(semilla, fondos.size)]
    val hilo = Color(0xFFFFF8F1)
    val sombra = Color(0xFFD9BFA5)
    val palo = Color(0xFF8A5F3F)
    Canvas(modifier = modifier.background(fondo)) {
        val w = size.width
        val h = size.height
        val lado = minOf(w, h)
        val arriba = h * 0.17f
        drawLine(
            color = palo,
            start = Offset(w * 0.18f, arriba),
            end = Offset(w * 0.82f, arriba),
            strokeWidth = lado * 0.035f,
            cap = StrokeCap.Round
        )
        when (Math.floorMod(semilla, 3)) {
            0 -> {
                // Tapiz con flecos.
                val n = 11
                for (i in 0 until n) {
                    val x = w * (0.24f + 0.52f * i / (n - 1))
                    val largo = h * (0.40f + 0.22f * (1f - Math.abs(i - n / 2) / (n / 2f)))
                    drawLine(
                        color = hilo,
                        start = Offset(x, arriba),
                        end = Offset(x, arriba + largo),
                        strokeWidth = lado * 0.02f,
                        cap = StrokeCap.Round
                    )
                }
                drawRect(
                    color = sombra,
                    topLeft = Offset(w * 0.24f, arriba + h * 0.1f),
                    size = Size(w * 0.52f, h * 0.012f)
                )
            }

            1 -> {
                // Atrapasueños circular.
                val centro = Offset(w / 2f, h * 0.5f)
                drawCircle(
                    color = hilo,
                    radius = lado * 0.24f,
                    center = centro,
                    style = Stroke(width = lado * 0.025f)
                )
                drawCircle(
                    color = hilo,
                    radius = lado * 0.13f,
                    center = centro,
                    style = Stroke(width = lado * 0.012f)
                )
                drawLine(hilo, Offset(w / 2f, arriba), Offset(w / 2f, centro.y - lado * 0.24f), lado * 0.012f)
                for (i in 0 until 3) {
                    val x = w * (0.4f + 0.1f * i)
                    drawLine(
                        color = hilo,
                        start = Offset(x, centro.y + lado * 0.22f),
                        end = Offset(x, centro.y + lado * (0.4f + 0.05f * i)),
                        strokeWidth = lado * 0.015f,
                        cap = StrokeCap.Round
                    )
                }
            }

            else -> {
                // Cortina/arco con forma triangular.
                val ruta = Path().apply {
                    moveTo(w * 0.24f, arriba)
                    lineTo(w * 0.76f, arriba)
                    lineTo(w * 0.5f, h * 0.82f)
                    close()
                }
                drawPath(ruta, color = hilo.copy(alpha = 0.9f))
                for (i in 1 until 5) {
                    val y = arriba + h * 0.12f * i
                    drawLine(
                        color = sombra,
                        start = Offset(w * (0.25f + 0.04f * i), y),
                        end = Offset(w * (0.75f - 0.04f * i), y),
                        strokeWidth = lado * 0.01f
                    )
                }
            }
        }
    }
}

/** Rama decorativa de hojas para fondos de bienvenida, registro e inicio de sesión. */
@Composable
fun RamaDecorativa(modifier: Modifier = Modifier, color: Color = Terracota.copy(alpha = 0.55f)) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val base = Offset(w * 0.5f, h)
        drawLine(color, base, Offset(w * 0.5f, h * 0.12f), strokeWidth = w * 0.012f, cap = StrokeCap.Round)
        for (i in 0 until 5) {
            val y = h * (0.2f + 0.16f * i)
            val ancho = w * (0.30f - 0.02f * i)
            val alto = h * 0.07f
            rotate(degrees = -35f, pivot = Offset(w * 0.5f, y)) {
                drawOval(color, Offset(w * 0.5f, y - alto / 2f), Size(ancho, alto))
            }
            rotate(degrees = 35f, pivot = Offset(w * 0.5f, y + h * 0.04f)) {
                drawOval(color, Offset(w * 0.5f - ancho, y + h * 0.04f - alto / 2f), Size(ancho, alto))
            }
        }
        drawOval(color, Offset(w * 0.5f - w * 0.05f, 0f), Size(w * 0.10f, h * 0.2f))
    }
}

/** Marca de la tienda: rama, nombre en serif y lema. */
@Composable
fun LogoMacrame(modifier: Modifier = Modifier, tamanoRama: Dp = 72.dp, mostrarLema: Boolean = true) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        RamaDecorativa(Modifier.size(width = tamanoRama * 0.7f, height = tamanoRama))
        Spacer(Modifier.height(8.dp))
        Text(
            "Lou Araya",
            style = MaterialTheme.typography.headlineLarge,
            color = Terracota
        )
        Text(
            "Macramé",
            style = MaterialTheme.typography.headlineLarge,
            color = Terracota
        )
        if (mostrarLema) {
            Spacer(Modifier.height(6.dp))
            Text(
                "Arte en fibras que decora tus historias",
                style = MaterialTheme.typography.bodyMedium,
                color = CafeSuave
            )
        }
    }
}

/** Avatar circular con iniciales (no hay fotografías de perfil guardadas). */
@Composable
fun AvatarIniciales(nombre: String, tamano: Dp = 56.dp, modifier: Modifier = Modifier) {
    val iniciales = nombre.trim().split(" ").filter { it.isNotBlank() }
        .take(2).joinToString("") { it.first().uppercase() }.ifEmpty { "?" }
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(TerracotaClaro),
        contentAlignment = Alignment.Center
    ) {
        Text(
            iniciales,
            style = MaterialTheme.typography.titleMedium,
            color = CafeProfundo
        )
    }
}
