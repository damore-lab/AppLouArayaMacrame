package cl.duoc.louarayamacrame.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cl.duoc.louarayamacrame.data.CampanaEntity
import cl.duoc.louarayamacrame.data.ProductoEntity
import cl.duoc.louarayamacrame.data.SolicitudEntity
import cl.duoc.louarayamacrame.validation.ValidacionesComercio
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import java.io.File

@Composable
fun Encabezado(titulo: String, volver: () -> Unit, contenido: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = volver) { Text("‹ Volver") }
            Text(
                titulo,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(8.dp))
        contenido()
    }
}

@Composable
fun TarjetaProducto(
    item: ProductoEntity,
    favorito: Boolean,
    onDetalle: () -> Unit,
    onFavorito: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable(onClick = onDetalle)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            ImagenProducto(item.imagenUri, Modifier.size(78.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "${item.categoria} · $${item.precio}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    if (item.disponible) "Disponible" else "A pedido",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onFavorito) { Text(if (favorito) "♥" else "♡") }
        }
    }
}

@Composable
fun ImagenProducto(ruta: String?, modifier: Modifier = Modifier) {
    val bitmap = remember(ruta) {
        try {
            ruta?.let { BitmapFactory.decodeFile(it) }
        } catch (_: Exception) {
            null
        }
    }
    if (bitmap != null) Image(
        bitmap.asImageBitmap(),
        contentDescription = "Fotografía del producto",
        modifier = modifier
    )
    else Box(
        modifier.background(
            MaterialTheme.colorScheme.secondaryContainer,
            RoundedCornerShape(12.dp)
        ), contentAlignment = Alignment.Center
    ) {
        Text(
            "✿",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun DashboardScreen(nombre: String, admin: Boolean, abrir: (String) -> Unit) {
    var mostrar by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { mostrar = true }
    Encabezado("Lou Araya Macramé", {}) {
        AnimatedVisibility(mostrar) {
            Column {
                Text("Hola, $nombre", style = MaterialTheme.typography.headlineMedium)
                Text(if (admin) "Panel de administración" else "Arte en fibras que decora tus historias")
                Spacer(Modifier.height(16.dp))
                Text(
                    "Descubre diseños artesanales únicos",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Spacer(Modifier.height(18.dp))
        val accesos = if (admin) listOf(
            "Catálogo",
            "Productos",
            "Campañas",
            "Solicitudes",
            "Ferias y mapa",
            "Escanear QR",
            "Perfil",
            "Notificaciones"
        )
        else listOf(
            "Catálogo",
            "Mis favoritos",
            "Solicitar cotización",
            "Mis solicitudes",
            "Ferias y mapa",
            "Escanear QR",
            "Notificaciones",
            "Perfil"
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(accesos) { titulo ->
                ElevatedCard(onClick = { abrir(titulo) }, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✿", color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(16.dp))
                        Text(titulo, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.weight(1f))
                        Text("›")
                    }
                }
            }
        }
        NavigationBar {
            listOf("Catálogo", "Ferias y mapa", "Mis solicitudes", "Perfil").forEach { acceso ->
                NavigationBarItem(
                    selected = false, onClick = { abrir(acceso) },
                    icon = {
                        Text(
                            when (acceso) {
                                "Catálogo" -> "✿"; "Ferias y mapa" -> "⌖"; "Mis solicitudes" -> "▤"; else -> "♙"
                            }
                        )
                    },
                    label = {
                        Text(
                            when (acceso) {
                                "Ferias y mapa" -> "Mapa"; "Mis solicitudes" -> "Solicitudes"; else -> acceso
                            }
                        )
                    })
            }
        }
    }
}

@Composable
fun CatalogoScreen(
    productos: List<ProductoEntity>,
    favoritos: Set<Int>,
    onDetalle: (Int) -> Unit,
    onFavorito: (Int) -> Unit,
    volver: () -> Unit,
    soloFavoritos: Boolean = false
) {
    var busqueda by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Todos") }
    val lista = productos.filter {
        (!soloFavoritos || it.id in favoritos) && (categoria == "Todos" || it.categoria == categoria) &&
                (it.nombre.contains(busqueda, true) || it.material.contains(busqueda, true))
    }
    Encabezado(if (soloFavoritos) "Mis favoritos" else "Catálogo artesanal", volver) {
        OutlinedTextField(
            busqueda,
            { busqueda = it },
            label = { Text("Buscar diseños o materiales") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .horizontalScroll(androidx.compose.foundation.rememberScrollState())
        ) {
            listOf("Todos", "Matrimonios", "Festivales", "Interiores").forEach { opcion ->
                FilterChip(
                    selected = categoria == opcion,
                    onClick = { categoria = opcion },
                    label = { Text(opcion, style = MaterialTheme.typography.labelSmall) })
            }
        }
        if (lista.isEmpty()) Text("No hay diseños para mostrar.")
        LazyColumn {
            items(lista, key = { it.id }) { item ->
                TarjetaProducto(
                    item,
                    item.id in favoritos,
                    { onDetalle(item.id) },
                    { onFavorito(item.id) })
            }
        }
    }
}

@Composable
fun DetalleScreen(
    item: ProductoEntity?,
    favorito: Boolean,
    onFavorito: () -> Unit,
    cotizar: () -> Unit,
    volver: () -> Unit
) {
    Encabezado("Detalle del diseño", volver) {
        if (item == null) {
            Text("Producto no encontrado"); return@Encabezado
        }
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            ImagenProducto(
                item.imagenUri, Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(item.nombre, style = MaterialTheme.typography.headlineSmall)
            Text(
                "$${item.precio} · ${item.categoria}",
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(10.dp))
            Text(item.descripcion)
            Text("Material: ${item.material}")
            Text("Dimensiones: ${item.dimensiones}")
            Text(if (item.disponible) "Disponible" else "Trabajo a pedido")
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onFavorito,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (favorito) "♥ Quitar de favoritos" else "♡ Agregar a favoritos") }
            Button(
                onClick = cotizar,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Solicitar cotización") }
        }
    }
}

@Composable
fun CotizacionScreen(
    usuarioId: Int,
    productoId: Int?,
    guardar: (SolicitudEntity) -> Unit,
    volver: () -> Unit
) {
    var evento by remember { mutableStateOf("") }
    var medidas by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var colores by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var validar by remember { mutableStateOf(false) }
    Encabezado("Solicitar cotización", volver) {
        Column(Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())) {
            @Composable
            fun campo(
                valor: String,
                cambio: (String) -> Unit,
                etiqueta: String,
                obligatorio: Boolean = true
            ) {
                val error = validar && obligatorio && valor.isBlank()
                OutlinedTextField(
                    valor,
                    cambio,
                    label = { Text(etiqueta) },
                    isError = error,
                    supportingText = { if (error) Text("Este campo es obligatorio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = etiqueta != "Observaciones"
                )
            }
            campo(evento, { evento = it }, "Tipo de evento")
            campo(medidas, { medidas = it }, "Medidas aproximadas")
            campo(material, { material = it }, "Material preferido")
            campo(colores, { colores = it }, "Colores")
            campo(fecha, { fecha = it }, "Fecha requerida (DD/MM/AAAA)")
            if (validar && !ValidacionesComercio.fechaValida(fecha)) Text(
                "Ingresa una fecha válida, desde hoy, en formato DD/MM/AAAA",
                color = MaterialTheme.colorScheme.error
            )
            campo(observaciones, { observaciones = it }, "Observaciones", false)
            Spacer(Modifier.height(12.dp))
            Button(onClick = {
                validar = true
                if (ValidacionesComercio.cotizacionValida(
                        evento,
                        medidas,
                        material,
                        colores,
                        fecha
                    )
                ) {
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
            }, modifier = Modifier.fillMaxWidth()) { Text("Enviar solicitud") }
        }
    }
}

@Composable
fun SolicitudesScreen(
    items: List<SolicitudEntity>,
    admin: Boolean,
    cambiar: (Int, String) -> Unit,
    volver: () -> Unit
) {
    Encabezado(if (admin) "Gestión de solicitudes" else "Mis solicitudes", volver) {
        if (items.isEmpty()) Text("Aún no hay solicitudes registradas.")
        LazyColumn {
            items(items, key = { it.id }) { solicitud ->
                ElevatedCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            "Solicitud #${solicitud.id} · ${solicitud.estado}",
                            fontWeight = FontWeight.Bold
                        )
                        Text("${solicitud.evento} · ${solicitud.medidas}")
                        Text("Material: ${solicitud.material} · ${solicitud.colores}")
                        Text("Fecha requerida: ${solicitud.fecha}")
                        if (solicitud.observaciones.isNotBlank()) Text(solicitud.observaciones)
                        if (admin) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf("Pendiente", "Confirmado", "Entregado").forEach { estado ->
                                    TextButton(onClick = { cambiar(solicitud.id, estado) }) {
                                        Text(
                                            estado,
                                            style = MaterialTheme.typography.labelSmall
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

@Composable
fun ProductosAdminScreen(
    productos: List<ProductoEntity>,
    guardar: (ProductoEntity) -> Unit,
    eliminar: (Int) -> Unit,
    volver: () -> Unit
) {
    val context = LocalContext.current
    var editando by remember { mutableStateOf<ProductoEntity?>(null) }
    var nombre by remember(editando) { mutableStateOf(editando?.nombre ?: "") }
    var descripcion by remember(editando) { mutableStateOf(editando?.descripcion ?: "") }
    var categoria by remember(editando) { mutableStateOf(editando?.categoria ?: "Interiores") }
    var material by remember(editando) { mutableStateOf(editando?.material ?: "") }
    var dimensiones by remember(editando) { mutableStateOf(editando?.dimensiones ?: "") }
    var precio by remember(editando) { mutableStateOf(editando?.precio?.toString() ?: "") }
    var disponible by remember(editando) { mutableStateOf(editando?.disponible ?: true) }
    var imagen by remember(editando) { mutableStateOf(editando?.imagenUri) }
    var errores by remember { mutableStateOf(false) }
    var confirmarEliminacion by remember { mutableStateOf(false) }
    val camara =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { foto: Bitmap? ->
            if (foto != null) {
                val archivo = File(context.filesDir, "producto_${System.currentTimeMillis()}.jpg")
                archivo.outputStream().use { foto.compress(Bitmap.CompressFormat.JPEG, 85, it) }
                imagen = archivo.absolutePath
            }
        }
    Encabezado("Gestión de productos", volver) {
        if (editando == null) {
            Button(onClick = {
                editando = ProductoEntity(
                    nombre = "",
                    descripcion = "",
                    categoria = "Interiores",
                    material = "",
                    dimensiones = "",
                    precio = 0
                )
            }) { Text("+ Crear producto") }
            LazyColumn {
                items(productos, key = { it.id }) { item ->
                    ElevatedCard(
                        onClick = { editando = item },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.nombre, modifier = Modifier.weight(1f)); Text("Editar ›")
                        }
                    }
                }
            }
        } else {
            Column(Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                @Composable
                fun campo(valor: String, cambio: (String) -> Unit, etiqueta: String) {
                    OutlinedTextField(
                        valor,
                        cambio,
                        label = { Text(etiqueta) },
                        isError = errores && valor.isBlank(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                campo(nombre, { nombre = it }, "Nombre")
                campo(descripcion, { descripcion = it }, "Descripción")
                campo(material, { material = it }, "Material")
                campo(dimensiones, { dimensiones = it }, "Dimensiones")
                campo(precio, { precio = it.filter(Char::isDigit) }, "Precio referencial")
                Text("Categoría")
                Row(Modifier.horizontalScroll(androidx.compose.foundation.rememberScrollState())) {
                    listOf("Interiores", "Matrimonios", "Festivales").forEach { opcion ->
                        FilterChip(
                            selected = categoria == opcion,
                            onClick = { categoria = opcion },
                            label = { Text(opcion, style = MaterialTheme.typography.labelSmall) })
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Disponible"); Switch(
                    disponible,
                    { disponible = it })
                }
                ImagenProducto(imagen, Modifier.size(120.dp))
                OutlinedButton(onClick = { camara.launch(null) }) { Text("Tomar fotografía (cámara)") }
                if (errores) Text(
                    "Completa los campos y usa un precio mayor que cero.",
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = {
                    errores = true
                    val numero = precio.toIntOrNull() ?: 0
                    if (ValidacionesComercio.productoValido(
                            nombre,
                            descripcion,
                            material,
                            dimensiones,
                            numero
                        )
                    ) {
                        guardar(
                            editando!!.copy(
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                categoria = categoria,
                                material = material.trim(),
                                dimensiones = dimensiones.trim(),
                                precio = numero,
                                disponible = disponible,
                                imagenUri = imagen
                            )
                        )
                        editando = null; errores = false
                    }
                }, modifier = Modifier.fillMaxWidth()) { Text("Guardar producto") }
                if (editando != null && editando!!.id > 0) {
                    OutlinedButton(
                        onClick = { confirmarEliminacion = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar producto")
                    }
                }
                TextButton(onClick = { editando = null }) { Text("Cancelar") }
            }
        }
    }

    if (confirmarEliminacion && editando != null) {
        AlertDialog(
            onDismissRequest = {
                confirmarEliminacion = false
            },
            title = {
                Text("Eliminar producto")
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar " +
                            "'${editando!!.nombre}'? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val id = editando!!.id
                        confirmarEliminacion = false
                        editando = null
                        eliminar(id)
                    }
                ) {
                    Text(
                        "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        confirmarEliminacion = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun CampanasScreen(
    campanas: List<CampanaEntity>,
    admin: Boolean,
    guardar: (CampanaEntity) -> Unit,
    volver: () -> Unit
) {
    var editando by remember { mutableStateOf<CampanaEntity?>(null) }
    var titulo by remember(editando) { mutableStateOf(editando?.titulo ?: "") }
    var descripcion by remember(editando) { mutableStateOf(editando?.descripcion ?: "") }
    var fecha by remember(editando) { mutableStateOf(editando?.fecha ?: "") }
    var activa by remember(editando) { mutableStateOf(editando?.activa ?: true) }
    var error by remember { mutableStateOf(false) }
    Encabezado(if (admin) "Colecciones y campañas" else "Notificaciones y novedades", volver) {
        if (admin && editando == null) Button(onClick = {
            editando = CampanaEntity(titulo = "", descripcion = "", fecha = "")
        }) { Text("+ Nueva campaña") }
        if (editando != null && admin) {
            Column(Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                OutlinedTextField(
                    titulo,
                    { titulo = it },
                    label = { Text("Nombre de campaña") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    descripcion,
                    { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    fecha,
                    { fecha = it },
                    label = { Text("Fecha o vigencia") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Publicada"); Switch(
                    activa,
                    { activa = it })
                }
                if (error) Text(
                    "Completa los tres campos.",
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = {
                    error = listOf(titulo, descripcion, fecha).any { it.isBlank() }
                    if (!error) {
                        guardar(
                            editando!!.copy(
                                titulo = titulo.trim(),
                                descripcion = descripcion.trim(),
                                fecha = fecha.trim(),
                                activa = activa
                            )
                        ); editando = null
                    }
                }) { Text("Guardar campaña") }
                TextButton(onClick = { editando = null }) { Text("Cancelar") }
            }
        } else {
            val visibles = if (admin) campanas else campanas.filter { it.activa }
            if (visibles.isEmpty()) Text("Todavía no hay novedades publicadas.")
            LazyColumn {
                items(visibles, key = { it.id }) { item ->
                    ElevatedCard(
                        onClick = { if (admin) editando = item },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(item.titulo, fontWeight = FontWeight.Bold)
                            Text(item.descripcion); Text("Vigencia: ${item.fecha}")
                            if (admin) Text(if (item.activa) "Publicada · Toca para editar" else "Borrador · Toca para editar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MapaFeriasScreen(volver: () -> Unit) {
    val context = LocalContext.current
    var ubicacion by remember { mutableStateOf("Ubicación no consultada") }
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
    Encabezado("Ferias y showrooms", volver) {
        Text("Espacios artesanales reales de referencia académica. No son puntos de venta oficiales de Lou Araya Macramé.")
        Spacer(Modifier.height(12.dp))
        listOf(
            "Centro Artesanal Santa Lucía · Santiago Centro" to
                    "Centro Artesanal Santa Lucía, Alameda 510, Santiago, Chile",

            "Pueblito Los Dominicos · Las Condes" to
                    "Centro Artesanal Los Dominicos, Apoquindo 9085, Las Condes, Chile",

            "Centro de Arte Indígena CENWE · Santiago Centro" to
                    "Centro de Exposición de Arte Indígena CENWE, Cerro Santa Lucía, Santiago, Chile"
        ).forEach { (nombre, coordenadas) ->
            ElevatedCard(
                onClick = {
                    val uri = Uri.parse("geo:0,0?q=${Uri.encode(coordenadas)}")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    try {
                        context.startActivity(intent)
                    } catch (_: Exception) { /* No hay aplicación de mapas instalada */
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        nombre,
                        fontWeight = FontWeight.Bold
                    ); Text("Ver ubicación en la aplicación de mapas ›")
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) leerUbicacion()
            else permiso.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }) { Text("Consultar mi ubicación") }
        Text(ubicacion)
    }
}

@Composable
fun EscanerQRScreen(abrirProducto: (Int) -> Unit, volver: () -> Unit) {
    val context = LocalContext.current
    var mensaje by remember { mutableStateOf("Escanea una etiqueta con el ID del producto, por ejemplo loumacrame:1") }
    val scanner = remember(context) { GmsBarcodeScanning.getClient(context) }
    Encabezado("Escáner QR", volver) {
        Text("Escanea el código QR de un producto para abrir su ficha.")
        Spacer(Modifier.height(18.dp))
        Button(onClick = {
            scanner.startScan().addOnSuccessListener { codigo ->
                val contenido = codigo.rawValue.orEmpty().trim()
                val id = contenido.removePrefix("loumacrame:").toIntOrNull()
                if (id != null && id > 0) abrirProducto(id) else mensaje =
                    "Código no reconocido: $contenido"
            }.addOnFailureListener {
                mensaje = "No se pudo leer el QR. Comprueba Google Play Services."
            }
                .addOnCanceledListener { mensaje = "Escaneo cancelado" }
        }, modifier = Modifier.fillMaxWidth()) { Text("Abrir cámara y escanear") }
        Spacer(Modifier.height(12.dp)); Text(mensaje)
    }
}
