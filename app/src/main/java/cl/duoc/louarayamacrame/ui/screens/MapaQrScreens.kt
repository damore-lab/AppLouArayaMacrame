package cl.duoc.louarayamacrame.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cl.duoc.louarayamacrame.ui.components.BotonMacrame
import cl.duoc.louarayamacrame.ui.components.CabeceraPantalla
import cl.duoc.louarayamacrame.ui.components.EtiquetaEstado
import cl.duoc.louarayamacrame.ui.components.FilaChips
import cl.duoc.louarayamacrame.ui.theme.CafeProfundo
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Terracota
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlin.math.hypot

private class Lugar(
    val nombre: String,
    val tipo: String,
    val comuna: String,
    val direccion: String,
    val x: Float,
    val y: Float
)

private val lugares = listOf(
    Lugar(
        "Centro Artesanal Santa Lucía", "Feria", "Santiago Centro",
        "Centro Artesanal Santa Lucía, Alameda 510, Santiago, Chile", 0.46f, 0.58f
    ),
    Lugar(
        "Pueblito Los Dominicos", "Feria", "Las Condes",
        "Centro Artesanal Los Dominicos, Apoquindo 9085, Las Condes, Chile", 0.80f, 0.28f
    ),
    Lugar(
        "Centro de Arte Indígena CENWE", "Exposición", "Santiago Centro",
        "Centro de Exposición de Arte Indígena CENWE, Cerro Santa Lucía, Santiago, Chile", 0.30f, 0.40f
    )
)

/**
 * Pantalla 5: ferias y showrooms. El mapa es un esquema dentro de la app; para ver el mapa real
 * se abre la aplicación de mapas del teléfono (igual que antes).
 */
@Composable
fun MapaFeriasScreen(volver: () -> Unit) {
    val context = LocalContext.current
    var ubicacion by remember { mutableStateOf("Ubicación no consultada") }
    var aviso by remember { mutableStateOf("") }
    var filtro by remember { mutableStateOf("Todos") }
    var seleccionado by remember { mutableStateOf(lugares.first().nombre) }

    fun leerUbicacion() {
        try {
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val permiso = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (!permiso) {
                ubicacion = "Permiso de ubicación no concedido"; return
            }
            val posicion = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?: manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ubicacion =
                if (posicion == null) "Sin ubicación reciente. Activa la ubicación del teléfono." else "Latitud: %.4f · Longitud: %.4f".format(
                    posicion.latitude,
                    posicion.longitude
                )
        } catch (_: Exception) {
            ubicacion = "No fue posible consultar la ubicación"
        }
    }

    val permiso =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
            if (concedido) leerUbicacion() else ubicacion = "Permiso denegado"
        }

    val visibles = lugares.filter {
        filtro == "Todos" || (filtro == "Ferias" && it.tipo == "Feria") ||
                (filtro == "Exposiciones" && it.tipo == "Exposición")
    }
    val elegido = visibles.find { it.nombre == seleccionado } ?: visibles.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CabeceraPantalla("Ferias y showrooms", volver)
        Column(Modifier.padding(horizontal = 16.dp)) {
            FilaChips(
                opciones = listOf("Todos", "Ferias", "Exposiciones"),
                seleccion = filtro,
                onSeleccion = { filtro = it }
            )
            Spacer(Modifier.height(10.dp))
        }
        MapaEsquematico(
            visibles = visibles,
            seleccionado = elegido?.nombre,
            onSeleccion = { seleccionado = it },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (elegido != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        elegido.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        color = CafeProfundo,
                        modifier = Modifier.weight(1f)
                    )
                    EtiquetaEstado(elegido.tipo)
                }
                Text(elegido.comuna, color = CafeSuave, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(10.dp))
                BotonMacrame("Ver ubicación en la aplicación de mapas", onClick = {
                    val uri = Uri.parse("geo:0,0?q=${Uri.encode(elegido.direccion)}")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    try {
                        context.startActivity(intent)
                        aviso = ""
                    } catch (_: Exception) {
                        aviso = "No hay una aplicación de mapas instalada."
                    }
                })
            }
            if (aviso.isNotEmpty()) {
                Text(aviso, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(8.dp))
            BotonMacrame("Consultar mi ubicación", secundario = true, onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) leerUbicacion()
                else permiso.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            })
            Spacer(Modifier.height(6.dp))
            Text(
                ubicacion,
                style = MaterialTheme.typography.bodySmall,
                color = CafeSuave,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                "Espacios artesanales reales de referencia académica. No son puntos de venta oficiales de Lou Araya Macramé.",
                style = MaterialTheme.typography.labelSmall,
                color = CafeSuave,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MapaEsquematico(
    visibles: List<Lugar>,
    seleccionado: String?,
    onSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .background(Color(0xFFEFE6D6))
            .pointerInput(visibles) {
                detectTapGestures { toque ->
                    val cerca = visibles.minByOrNull {
                        hypot(toque.x - it.x * size.width, toque.y - it.y * size.height)
                    }
                    if (cerca != null &&
                        hypot(toque.x - cerca.x * size.width, toque.y - cerca.y * size.height) < 90f
                    ) onSeleccion(cerca.nombre)
                }
            }
    ) {
        val w = size.width
        val h = size.height
        // Parque y río de fondo.
        drawRect(Color(0xFFDCE5C8), Offset(w * 0.05f, h * 0.62f), Size(w * 0.28f, h * 0.18f))
        val rio = Path().apply {
            moveTo(0f, h * 0.25f)
            cubicTo(w * 0.3f, h * 0.15f, w * 0.5f, h * 0.45f, w * 0.7f, h * 0.35f)
            cubicTo(w * 0.85f, h * 0.28f, w * 0.95f, h * 0.4f, w, h * 0.38f)
        }
        drawPath(rio, Color(0xFFBFD7E3), style = Stroke(width = w * 0.03f, cap = StrokeCap.Round))
        // Calles.
        val calle = Color(0xFFFFFAF2)
        for (i in 1..4) {
            drawLine(calle, Offset(0f, h * 0.2f * i), Offset(w, h * (0.2f * i - 0.04f)), strokeWidth = w * 0.014f)
        }
        for (i in 1..4) {
            drawLine(calle, Offset(w * 0.2f * i, 0f), Offset(w * (0.2f * i + 0.05f), h), strokeWidth = w * 0.014f)
        }
        drawLine(Color(0xFFF5D9A8), Offset(0f, h * 0.7f), Offset(w, h * 0.5f), strokeWidth = w * 0.03f)
        // Marcadores.
        visibles.forEach { lugar ->
            val centro = Offset(lugar.x * w, lugar.y * h)
            val activo = lugar.nombre == seleccionado
            val radio = if (activo) w * 0.045f else w * 0.032f
            drawCircle(Color(0x33000000), radio * 1.15f, centro + Offset(2f, 4f))
            drawCircle(if (activo) Terracota else Color(0xFFC27B4F), radio, centro)
            drawCircle(Color.White, radio * 0.4f, centro)
        }
    }
}

/** Pantalla 8: escáner QR. Abre el lector de Google Play Services igual que antes. */
@Composable
fun EscanerQRScreen(abrirProducto: (Int) -> Unit, volver: () -> Unit) {
    val context = LocalContext.current
    var mensaje by remember { mutableStateOf("Escanea una etiqueta con el ID del producto, por ejemplo loumacrame:1") }
    val scanner = remember(context) { GmsBarcodeScanning.getClient(context) }

    val transicion = rememberInfiniteTransition(label = "barrido")
    val barrido by transicion.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Reverse),
        label = "posicionBarrido"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2A1A14))
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = volver) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Text(
                "Escanear código QR",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(Modifier.size(260.dp)) {
                val lado = size.width
                val largo = lado * 0.18f
                val grosor = 6f
                val c = Color.White
                // Cuatro esquinas del visor.
                fun esquina(x: Float, y: Float, dx: Float, dy: Float) {
                    drawLine(c, Offset(x, y), Offset(x + dx * largo, y), grosor, StrokeCap.Round)
                    drawLine(c, Offset(x, y), Offset(x, y + dy * largo), grosor, StrokeCap.Round)
                }
                esquina(0f, 0f, 1f, 1f)
                esquina(lado, 0f, -1f, 1f)
                esquina(0f, lado, 1f, -1f)
                esquina(lado, lado, -1f, -1f)
                // Línea de barrido animada.
                val y = lado * barrido
                drawLine(
                    Terracota,
                    Offset(lado * 0.06f, y),
                    Offset(lado * 0.94f, y),
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(
                "Enfoca el código QR del producto o etiqueta de la feria",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                mensaje,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            BotonMacrame("Abrir cámara y escanear", onClick = {
                scanner.startScan().addOnSuccessListener { codigo ->
                    val contenido = codigo.rawValue.orEmpty().trim()
                    val id = contenido.removePrefix("loumacrame:").toIntOrNull()
                    if (id != null && id > 0) abrirProducto(id) else mensaje =
                        "Código no reconocido: $contenido"
                }.addOnFailureListener {
                    mensaje = "No se pudo leer el QR. Comprueba Google Play Services."
                }
                    .addOnCanceledListener { mensaje = "Escaneo cancelado" }
            })
        }
    }
}
