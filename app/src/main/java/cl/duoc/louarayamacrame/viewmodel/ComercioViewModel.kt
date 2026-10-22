package cl.duoc.louarayamacrame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.louarayamacrame.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/* revisar start
        Punto 8: separar catálogo, formularios, solicitudes y campañas por responsabilidad,
        compartir repositorios locales y exponer StateFlow de solo lectura.
        Puntos 1 y 5: recibir una sesión restaurada, limpiar datos al cerrar sesión
        y verificar usuario y rol dentro de las operaciones, no solo en las pantallas.
        Puntos 12, 14, 16 y 17: crear los componentes de lógica que faltan para
        colecciones, avisos, eventos e historial QR; todos con persistencia local.
 */
class ComercioViewModel : ViewModel() {
    private var dao: ComercioDao? = null
    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()
    val productos = MutableStateFlow<List<ProductoEntity>>(emptyList())
    val campanas = MutableStateFlow<List<CampanaEntity>>(emptyList())
    val solicitudes = MutableStateFlow<List<SolicitudEntity>>(emptyList())
    val favoritos = MutableStateFlow<Set<Int>>(emptySet())
    private var usuarioActual = -1
    private var favoritosJob: Job? = null
    private var solicitudesJob: Job? = null
    /* revisar start
            Puntos 8 y 19: llevar la carga inicial a la capa de datos e incluir imágenes
            de ejemplo locales. Separar datos compartidos del estado de cada usuario.
     */
    fun configurar(database: AppDatabase, usuarioId: Int, admin: Boolean) {
        if (dao == null) {
            dao = database.comercioDao()
            viewModelScope.launch {
                val d = dao ?: return@launch
                withContext(Dispatchers.IO) {
                    if (d.contarProductos() == 0) {
                        listOf(
                            ProductoEntity(
                                nombre = "Tapiz Mural Sol Andino",
                                descripcion = "Tapiz artesanal tejido a mano, ideal para interiores cálidos.",
                                categoria = "Interiores",
                                material = "Algodón natural",
                                dimensiones = "90 x 70 cm",
                                precio = 65000
                            ),
                            ProductoEntity(
                                nombre = "Arco de ceremonia",
                                descripcion = "Arco decorativo para bodas y eventos especiales.",
                                categoria = "Matrimonios",
                                material = "Algodón y madera",
                                dimensiones = "200 x 220 cm",
                                precio = 120000
                            ),
                            ProductoEntity(
                                nombre = "Macetero colgante",
                                descripcion = "Soporte decorativo para plantas.",
                                categoria = "Interiores",
                                material = "Algodón",
                                dimensiones = "80 x 30 cm",
                                precio = 28000
                            ),
                            ProductoEntity(
                                nombre = "Atrapasueños artesanal",
                                descripcion = "Diseño bohemio para ferias y festivales.",
                                categoria = "Festivales",
                                material = "Algodón y madera",
                                dimensiones = "40 x 60 cm",
                                precio = 22000
                            ),
                            ProductoEntity(
                                nombre = "Cortina decorativa",
                                descripcion = "Cortina tejida por encargo.",
                                categoria = "Interiores",
                                material = "Algodón",
                                dimensiones = "150 x 200 cm",
                                precio = 85000
                            )
                        ).forEach { d.crearProducto(it) }
                    }
                }
            }
            viewModelScope.launch { dao!!.productos().collect { productos.value = it } }
            viewModelScope.launch { dao!!.campanas().collect { campanas.value = it } }
        }
        if (usuarioActual != usuarioId) {
            usuarioActual = usuarioId
            favoritosJob?.cancel()
            solicitudesJob?.cancel()
            favoritos.value = emptySet()
            solicitudes.value = emptyList()
            favoritosJob = viewModelScope.launch {
                dao!!.favoritos(usuarioId)
                    .collect { favoritos.value = it.map { f -> f.productoId }.toSet() }
            }
            solicitudesJob = viewModelScope.launch {
                (if (admin) dao!!.todasSolicitudes() else dao!!.misSolicitudes(usuarioId))
                    .collect { solicitudes.value = it }
            }
        }
    }

    //revisar end

    fun limpiarMensaje() {
        _mensaje.value = ""
    }

    fun favorito(usuarioId: Int, productoId: Int) = ejecutar {
        val d = dao ?: return@ejecutar
        if (favoritos.value.contains(productoId)) d.quitarFavorito(usuarioId, productoId)
        else d.agregarFavorito(FavoritoEntity(usuarioId, productoId))
    }

    /* revisar start
            Puntos 2, 3 y 5: publicar estado de guardado y un resultado de éxito o error,
            bloquear operaciones repetidas y comprobar permisos antes de escribir.
            Punto 4: desactivar sin confundir con trabajo a pedido; impedir que una
            eliminación deje solicitudes sin información histórica del producto.
            Puntos 13, 14 y 18: validar vigencia y transiciones de estado y generar
            avisos locales por usuario dentro de una operación coherente en Room.
     */
    fun guardarProducto(item: ProductoEntity) = ejecutar {
        val d = dao ?: return@ejecutar
        if (item.id == 0) d.crearProducto(item) else d.editarProducto(item)
        _mensaje.value = "Producto guardado correctamente"
    }

    fun eliminarProducto(productoId: Int) = ejecutar {
        val d = dao ?: return@ejecutar

        if (productoId <= 0) return@ejecutar

        d.eliminarProductoCompleto(productoId)

        _mensaje.value = "Producto eliminado correctamente"
    }

    fun solicitar(item: SolicitudEntity) = ejecutar {
        dao?.crearSolicitud(item)
        _mensaje.value = "Solicitud enviada correctamente"
    }

    fun cambiarEstado(id: Int, estado: String) = ejecutar {
        dao?.cambiarEstado(id, estado)
        _mensaje.value = "Estado actualizado"
    }

    fun guardarCampana(item: CampanaEntity) = ejecutar {
        if (item.id == 0) dao?.crearCampana(item) else dao?.editarCampana(item)
        _mensaje.value = "Campaña guardada"
    }

    //revisar end

    /* revisar start
            Puntos 2 y 3: propagar el resultado de la operación al formulario y liberar
            el estado de guardado incluso ante error. No anunciar éxito si el DAO
            todavía no está disponible o si no se realizó ninguna escritura.
     */
    private fun ejecutar(accion: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { accion() }
            } catch (e: Exception) {
                _mensaje.value = "No se pudo completar la operación"
            }
        }
    }
    //revisar end
}
//revisar end
