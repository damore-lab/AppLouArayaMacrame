package cl.duoc.louarayamacrame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.louarayamacrame.data.ProductoEntity
import cl.duoc.louarayamacrame.ui.components.BotonMacrame
import cl.duoc.louarayamacrame.ui.components.CabeceraPantalla
import cl.duoc.louarayamacrame.ui.components.CampoMacrame
import cl.duoc.louarayamacrame.ui.components.EtiquetaEstado
import cl.duoc.louarayamacrame.ui.components.FilaChips
import cl.duoc.louarayamacrame.ui.components.ImagenProducto
import cl.duoc.louarayamacrame.ui.components.formatoPrecio
import cl.duoc.louarayamacrame.ui.theme.CafeProfundo
import cl.duoc.louarayamacrame.ui.theme.CafeSuave
import cl.duoc.louarayamacrame.ui.theme.Terracota

/** Tarjeta de producto para la galería (pantallas 6 y 9). */
@Composable
fun TarjetaProducto(
    item: ProductoEntity,
    favorito: Boolean,
    onDetalle: () -> Unit,
    onFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onDetalle),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box {
            ImagenProducto(
                item.imagenUri,
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                semilla = item.id
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.92f))
                    .clickable(onClick = onFavorito),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (favorito) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = Terracota,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(
                item.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = CafeProfundo,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                item.categoria,
                style = MaterialTheme.typography.bodySmall,
                color = CafeSuave
            )
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    formatoPrecio(item.precio),
                    color = Terracota,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                EtiquetaEstado(if (item.disponible) "Disponible" else "A pedido")
            }
        }
    }
}

/** Pantallas 6 (Catálogo) y 9 (Mis favoritos): galería con búsqueda y filtros. */
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CabeceraPantalla(
            titulo = if (soloFavoritos) "Mis favoritos" else "Catálogo",
            onVolver = volver
        )
        Column(Modifier.padding(horizontal = 16.dp)) {
            CampoMacrame(
                valor = busqueda,
                onCambio = { busqueda = it },
                etiqueta = "Buscar diseños o materiales",
                icono = Icons.Filled.Search
            )
            Spacer(Modifier.height(4.dp))
            FilaChips(
                opciones = listOf("Todos", "Matrimonios", "Festivales", "Interiores"),
                seleccion = categoria,
                onSeleccion = { categoria = it }
            )
            Spacer(Modifier.height(10.dp))
        }
        if (lista.isEmpty()) {
            Text(
                if (soloFavoritos) "Aún no tienes favoritos. Toca el corazón de un diseño para guardarlo."
                else "No hay diseños para mostrar.",
                color = CafeSuave,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(lista, key = { it.id }) { item ->
                TarjetaProducto(
                    item,
                    item.id in favoritos,
                    { onDetalle(item.id) },
                    { onFavorito(item.id) }
                )
            }
        }
    }
}

/** Pantalla 7: detalle del producto con foto grande y acciones según el rol. */
@Composable
fun DetalleScreen(
    item: ProductoEntity?,
    favorito: Boolean,
    onFavorito: () -> Unit,
    cotizar: () -> Unit,
    volver: () -> Unit,
    admin: Boolean = false
) {
    if (item == null) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CabeceraPantalla("Detalle del diseño", volver)
            Text(
                "Producto no encontrado",
                color = CafeSuave,
                modifier = Modifier.padding(24.dp)
            )
        }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                ImagenProducto(
                    item.imagenUri,
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    semilla = item.id
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f))
                        .clickable(onClick = volver),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = CafeProfundo
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f))
                        .clickable(onClick = onFavorito),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (favorito) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = Terracota
                    )
                }
            }
            Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    item.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    color = CafeProfundo
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        formatoPrecio(item.precio),
                        style = MaterialTheme.typography.titleLarge,
                        color = Terracota,
                        modifier = Modifier.weight(1f)
                    )
                    EtiquetaEstado(if (item.disponible) "Disponible" else "A pedido")
                }
                Spacer(Modifier.height(14.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    FilaDato("Categoría", item.categoria)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaDato("Dimensiones", item.dimensiones)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaDato("Material", item.material)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaDato("Estado", if (item.disponible) "Disponible" else "Trabajo a pedido")
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    item.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = CafeProfundo
                )
            }
        }
        if (!admin) {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                BotonMacrame("Solicitar cotización", cotizar)
            }
        }
    }
}

@Composable
private fun FilaDato(titulo: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Text(
            titulo,
            style = MaterialTheme.typography.titleMedium,
            color = CafeProfundo,
            modifier = Modifier.width(116.dp)
        )
        Text(valor, color = CafeSuave, modifier = Modifier.weight(1f))
    }
}
