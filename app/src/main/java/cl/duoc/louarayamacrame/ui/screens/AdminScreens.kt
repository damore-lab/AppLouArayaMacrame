package cl.duoc.louarayamacrame.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.louarayamacrame.data.CampanaEntity
import cl.duoc.louarayamacrame.data.ProductoEntity
import cl.duoc.louarayamacrame.ui.components.BotonMacrame
import cl.duoc.louarayamacrame.ui.components.CabeceraPantalla
import cl.duoc.louarayamacrame.ui.components.CampoMacrame
import cl.duoc.louarayamacrame.ui.components.EtiquetaEstado
import cl.duoc.louarayamacrame.ui.components.FilaChips
import cl.duoc.louarayamacrame.ui.components.IlustracionMacrame
import cl.duoc.louarayamacrame.ui.components.ImagenProducto
import cl.duoc.louarayamacrame.ui.components.formatoPrecio
import cl.duoc.louarayamacrame.ui.theme.CafeProfundo
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Oliva
import cl.duoc.louarayamacrame.ui.theme.Terracota
import cl.duoc.louarayamacrame.validation.ValidacionesComercio
import java.io.File

/** Pantalla 11: gestión de productos (lista con interruptor de disponibilidad y editor). */
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
    var busqueda by remember { mutableStateOf("") }
    var filtro by remember { mutableStateOf("Todos") }

    val camara =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { foto: Bitmap? ->
            if (foto != null) {
                val archivo = File(context.filesDir, "producto_${System.currentTimeMillis()}.jpg")
                archivo.outputStream().use { foto.compress(Bitmap.CompressFormat.JPEG, 85, it) }
                imagen = archivo.absolutePath
            }
        }

    val actual = editando
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (actual == null) {
            CabeceraPantalla("Gestión de productos", volver)
            Column(Modifier.padding(horizontal = 16.dp)) {
                CampoMacrame(
                    busqueda, { busqueda = it }, "Buscar productos…",
                    icono = Icons.Filled.Search
                )
                Spacer(Modifier.height(4.dp))
                FilaChips(
                    opciones = listOf("Todos", "Disponibles", "A pedido"),
                    seleccion = filtro,
                    onSeleccion = { filtro = it }
                )
                Spacer(Modifier.height(10.dp))
            }
            val lista = productos.filter {
                (filtro == "Todos" || (filtro == "Disponibles") == it.disponible) &&
                        it.nombre.contains(busqueda, true)
            }
            if (lista.isEmpty()) {
                Text(
                    "No hay productos para mostrar.",
                    color = CafeSuave,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(lista, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ImagenProducto(item.imagenUri, Modifier.size(64.dp), semilla = item.id)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    item.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = CafeProfundo,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    formatoPrecio(item.precio),
                                    color = Terracota,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    if (item.disponible) "Disponible" else "A pedido",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CafeSuave
                                )
                            }
                            Switch(
                                checked = item.disponible,
                                onCheckedChange = { guardar(item.copy(disponible = it)) },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = Oliva,
                                    checkedThumbColor = Color.White
                                )
                            )
                            IconButton(onClick = { editando = item }) {
                                Icon(
                                    Icons.Filled.Create,
                                    contentDescription = "Editar ${item.nombre}",
                                    tint = CafeSuave
                                )
                            }
                        }
                    }
                }
            }
            Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                BotonMacrame("+ Crear nuevo producto", onClick = {
                    editando = ProductoEntity(
                        nombre = "",
                        descripcion = "",
                        categoria = "Interiores",
                        material = "",
                        dimensiones = "",
                        precio = 0
                    )
                })
            }
        } else {
            CabeceraPantalla(
                if (actual.id > 0) "Editar producto" else "Nuevo producto",
                onVolver = { editando = null; errores = false }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                CampoMacrame(
                    nombre, { nombre = it }, "Nombre",
                    error = if (errores && nombre.isBlank()) "Este campo es obligatorio" else ""
                )
                CampoMacrame(
                    descripcion, { descripcion = it }, "Descripción",
                    error = if (errores && descripcion.isBlank()) "Este campo es obligatorio" else ""
                )
                CampoMacrame(
                    material, { material = it }, "Material",
                    error = if (errores && material.isBlank()) "Este campo es obligatorio" else ""
                )
                CampoMacrame(
                    dimensiones, { dimensiones = it }, "Dimensiones",
                    error = if (errores && dimensiones.isBlank()) "Este campo es obligatorio" else ""
                )
                CampoMacrame(
                    precio, { precio = it.filter(Char::isDigit) }, "Precio referencial",
                    teclado = KeyboardType.Number,
                    error = if (errores && (precio.toIntOrNull() ?: 0) <= 0) "Ingresa un precio mayor que cero" else ""
                )
                Text("Categoría", style = MaterialTheme.typography.titleMedium, color = CafeProfundo)
                Spacer(Modifier.height(6.dp))
                FilaChips(
                    opciones = listOf("Interiores", "Matrimonios", "Festivales"),
                    seleccion = categoria,
                    onSeleccion = { categoria = it }
                )
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Disponible",
                        style = MaterialTheme.typography.titleMedium,
                        color = CafeProfundo,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = disponible,
                        onCheckedChange = { disponible = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Oliva,
                            checkedThumbColor = Color.White
                        )
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ImagenProducto(imagen, Modifier.size(110.dp), semilla = actual.id)
                    Spacer(Modifier.width(14.dp))
                    BotonMacrame(
                        "Tomar fotografía",
                        onClick = { camara.launch(null) },
                        secundario = true
                    )
                }
                if (errores && !ValidacionesComercio.productoValido(
                        nombre, descripcion, material, dimensiones, precio.toIntOrNull() ?: 0
                    )
                ) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Completa los campos y usa un precio mayor que cero.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BotonMacrame("Guardar producto", onClick = {
                    errores = true
                    val numero = precio.toIntOrNull() ?: 0
                    if (ValidacionesComercio.productoValido(
                            nombre, descripcion, material, dimensiones, numero
                        )
                    ) {
                        guardar(
                            actual.copy(
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
                })
                if (actual.id > 0) {
                    BotonMacrame(
                        "Eliminar producto",
                        onClick = { confirmarEliminacion = true },
                        secundario = true,
                        peligro = true
                    )
                }
                TextButton(
                    onClick = { editando = null; errores = false },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) { Text("Cancelar", color = CafeSuave) }
            }
        }
    }

    if (confirmarEliminacion && editando != null) {
        AlertDialog(
            onDismissRequest = { confirmarEliminacion = false },
            title = { Text("Eliminar producto") },
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
                ) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminacion = false }) { Text("Cancelar") }
            }
        )
    }
}

/** Pantalla 12: gestión de campañas (solo administración). */
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

    val actual = editando
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CabeceraPantalla(
            titulo = if (actual != null) {
                if (actual.id > 0) "Editar campaña" else "Nueva campaña"
            } else if (admin) "Colecciones y campañas" else "Novedades",
            onVolver = if (actual != null) ({ editando = null; error = false }) else volver
        )
        if (actual != null && admin) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                CampoMacrame(
                    titulo, { titulo = it }, "Nombre de campaña",
                    error = if (error && titulo.isBlank()) "Este campo es obligatorio" else ""
                )
                CampoMacrame(
                    descripcion, { descripcion = it }, "Descripción",
                    error = if (error && descripcion.isBlank()) "Este campo es obligatorio" else ""
                )
                CampoMacrame(
                    fecha, { fecha = it }, "Fecha o vigencia",
                    error = if (error && fecha.isBlank()) "Este campo es obligatorio" else ""
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Publicada",
                        style = MaterialTheme.typography.titleMedium,
                        color = CafeProfundo,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = activa,
                        onCheckedChange = { activa = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Oliva,
                            checkedThumbColor = Color.White
                        )
                    )
                }
                if (error) {
                    Spacer(Modifier.height(6.dp))
                    Text("Completa los tres campos.", color = MaterialTheme.colorScheme.error)
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BotonMacrame("Guardar campaña", onClick = {
                    error = listOf(titulo, descripcion, fecha).any { it.isBlank() }
                    if (!error) {
                        guardar(
                            actual.copy(
                                titulo = titulo.trim(),
                                descripcion = descripcion.trim(),
                                fecha = fecha.trim(),
                                activa = activa
                            )
                        ); editando = null
                    }
                })
                TextButton(
                    onClick = { editando = null; error = false },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) { Text("Cancelar", color = CafeSuave) }
            }
        } else {
            val visibles = if (admin) campanas else campanas.filter { it.activa }
            if (visibles.isEmpty()) {
                Text(
                    "Todavía no hay novedades publicadas.",
                    color = CafeSuave,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(visibles, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = admin) { editando = item },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IlustracionMacrame(
                                item.id,
                                Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    item.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = CafeProfundo
                                )
                                Text(
                                    item.descripcion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CafeSuave,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    EtiquetaEstado(if (item.activa) "Publicada" else "Borrador")
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Vigencia: ${item.fecha}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CafeSuave
                                    )
                                }
                            }
                            if (admin) Text("Editar", color = Terracota, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            if (admin) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    BotonMacrame("+ Nueva campaña", onClick = {
                        editando = CampanaEntity(titulo = "", descripcion = "", fecha = "")
                    })
                }
            }
        }
    }
}
