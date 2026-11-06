package cl.duoc.louarayamacrame.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import cl.duoc.louarayamacrame.ui.components.BarraInferior
import cl.duoc.louarayamacrame.ui.components.rutasConBarra
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.louarayamacrame.data.*
import cl.duoc.louarayamacrame.ui.screens.*
import cl.duoc.louarayamacrame.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = remember(context) { AppDatabase.obtenerBaseDatos(context) }
    /* revisar start
            los valores pueden no quedar guardados o no restaurarse y generar errores.
     */
    var usuarioAutenticadoId by remember { mutableStateOf<Int?>(null) }
    var usuario by remember { mutableStateOf<UsuarioEntity?>(null) }
    var productoSeleccionado by remember { mutableIntStateOf(0) }
    //revisar end
    val comercio: ComercioViewModel = viewModel()
    val productos by comercio.productos.collectAsStateWithLifecycle()
    val favoritos by comercio.favoritos.collectAsStateWithLifecycle()
    val solicitudes by comercio.solicitudes.collectAsStateWithLifecycle()
    val campanas by comercio.campanas.collectAsStateWithLifecycle()
    val mensaje by comercio.mensaje.collectAsStateWithLifecycle()

    /* revisar start
            Punto 8: trasladar la creación del administrador de demostración a la capa
            de datos y la carga del usuario a un componente de sesión. La navegación
            debe observar el estado de sesión, sin consultar ni inicializar usuarios.
     */
    LaunchedEffect(database) {
        withContext(Dispatchers.IO) {
            val dao = database.usuarioDao()
            if (dao.buscarPorCorreo("admin@loumacrame.demo") == null) {
                val salt = SeguridadContrasena.generarSalt()
                val hash = SeguridadContrasena.generarHash("AdminDemo2026!", salt)
                try {
                    dao.insertarUsuario(
                        UsuarioEntity(
                            nombre = "Administradora Demo",
                            alias = "admin_lou",
                            correo = "admin@loumacrame.demo",
                            contrasenaHash = hash,
                            contrasenaSalt = salt,
                            rol = "ADMIN"
                        )
                    )
                } catch (_: Exception) { /* Cuenta ya existente */
                }
            }
        }
    }
    LaunchedEffect(usuarioAutenticadoId) {
        val id = usuarioAutenticadoId
        if (id != null) {
            usuario = withContext(Dispatchers.IO) { database.usuarioDao().buscarPorId(id) }
            if (usuario == null) usuarioAutenticadoId = null
            else comercio.configurar(database, id, usuario!!.rol == "ADMIN")
        } else usuario = null
    }
    //revisar end
    fun ir(ruta: String) {
        navController.navigate(ruta) { launchSingleTop = true }
    }

    // Cambio entre secciones desde la barra inferior: vuelve al inicio y abre la sección elegida.
    fun irBarra(ruta: String) {
        navController.navigate(ruta) {
            popUpTo("principal") { inclusive = false }
            launchSingleTop = true
        }
    }
    val rutaActual = navController.currentBackStackEntryAsState().value?.destination?.route

    /* revisar start
            Puntos 1 y 5: al salir, limpiar sesión y estado del usuario anterior.
            Al restaurar la app, comprobar la sesión y el rol antes de mostrar
            cualquier destino protegido; evitar pantallas vacías si no hay usuario.
     */
    fun salir() {
        usuarioAutenticadoId = null
        navController.navigate(Rutas.LOGIN) {
            popUpTo(Rutas.BIENVENIDA) { inclusive = false }
            launchSingleTop = true
        }
    }

    //revisar end

    /* revisar start
            Puntos 1, 5 y 9: centralizar todas las rutas en Rutas.kt y pasar los ID
            de producto como argumentos restaurables. Compartir la barra de navegación
            entre secciones, indicando el destino seleccionado y respetando el rol.
            Puntos 12, 14, 16 y 17: incorporar destinos locales para colecciones,
            notificaciones, eventos y consultas desde QR; aún no tienen rutas propias.
     */
    val usuarioActual = usuario
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (usuarioActual != null && rutaActual in rutasConBarra) {
                BarraInferior(rutaActual, usuarioActual.rol == "ADMIN") { irBarra(it) }
            }
        }
    ) { relleno ->
    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA,
        modifier = Modifier.padding(relleno),
        enterTransition = { fadeIn(tween(260)) + slideInHorizontally(tween(260)) { it / 12 } },
        exitTransition = { fadeOut(tween(180)) },
        popEnterTransition = { fadeIn(tween(260)) },
        popExitTransition = { fadeOut(tween(180)) + slideOutHorizontally(tween(260)) { it / 12 } }
    ) {
        composable(Rutas.BIENVENIDA) {
            InicioScreen(onLogin = { ir(Rutas.LOGIN) }, onRegistro = { ir(Rutas.REGISTRO) })
        }
        composable(Rutas.LOGIN) {
            val vm: LoginViewModel = viewModel()
            val id by vm.usuarioId.collectAsStateWithLifecycle()
            LaunchedEffect(vm, database) { vm.configurarDao(database.usuarioDao()) }
            LoginScreen(
                viewModel = vm,
                onRegistro = { ir(Rutas.REGISTRO) },
                onVolver = { navController.popBackStack() },
                onLoginExitoso = {
                    if (id != null) {
                        usuarioAutenticadoId = id
                        navController.navigate("principal") {
                            popUpTo(Rutas.LOGIN) {
                                inclusive = true
                            }; launchSingleTop = true
                        }
                    }
                })
        }
        composable(Rutas.REGISTRO) {
            val vm: RegistroViewModel = viewModel()
            LaunchedEffect(vm, database) { vm.configurarDao(database.usuarioDao()) }
            RegistroScreen(viewModel = vm, onVolver = { navController.popBackStack() })
        }
        composable("principal") {
            val actual = usuario
            if (actual == null) {
                LaunchedEffect(usuarioAutenticadoId) { if (usuarioAutenticadoId == null) salir() }
            } else DashboardScreen(
                nombre = actual.nombre,
                admin = actual.rol == "ADMIN",
                abrir = { opcion ->
                    when (opcion) {
                        "Catálogo" -> ir("catalogo")
                        "Mis favoritos" -> ir("favoritos")
                        "Solicitar cotización" -> {
                            productoSeleccionado = 0; ir("cotizar")
                        }

                        "Mis solicitudes", "Solicitudes" -> ir("solicitudes")
                        "Ferias y mapa" -> ir("mapa")
                        "Escanear QR" -> ir("qr")
                        "Perfil" -> ir(Rutas.PERFIL)
                        "Productos" -> ir("productos_admin")
                        "Campañas", "Notificaciones" -> ir(if (opcion == "Campañas") "campanas_admin" else "novedades")
                    }
                },
                campanas = campanas,
                productos = productos,
                abrirProducto = { productoSeleccionado = it; ir("detalle") }
            )
        }
        composable(Rutas.PERFIL) {
            val actual = usuario
            val vm: PerfilViewModel = viewModel()
            if (actual != null) PerfilScreen(
                usuario = actual, viewModel = vm,
                onGuardar = { nombre, alias ->
                    vm.actualizarPerfil(
                        actual,
                        nombre,
                        alias,
                        database.usuarioDao()
                    ) { actualizado -> usuario = actualizado }
                },
                onCerrarSesion = { salir() },
                onVolver = { navController.popBackStack() })
            else LaunchedEffect(Unit) { ir(Rutas.LOGIN) }
        }
        composable("catalogo") {
            if (usuario != null) CatalogoScreen(
                productos,
                favoritos,
                { productoSeleccionado = it; ir("detalle") },
                { comercio.favorito(usuario!!.id, it) },
                { navController.popBackStack() })
        }
        composable("favoritos") {
            if (usuario != null && usuario!!.rol != "ADMIN") CatalogoScreen(
                productos,
                favoritos,
                { productoSeleccionado = it; ir("detalle") },
                { comercio.favorito(usuario!!.id, it) },
                { navController.popBackStack() },
                true
            )
        }
        composable("detalle") {
            if (usuario != null) DetalleScreen(
                productos.find { it.id == productoSeleccionado },
                productoSeleccionado in favoritos,
                { comercio.favorito(usuario!!.id, productoSeleccionado) },
                { if (usuario!!.rol != "ADMIN") ir("cotizar") },
                { navController.popBackStack() },
                usuario!!.rol == "ADMIN")
        }
        composable("cotizar") {
            if (usuario != null && usuario!!.rol != "ADMIN") CotizacionScreen(
                usuario!!.id,
                productoSeleccionado.takeIf { it > 0 },
                /* revisar start
                        Puntos 2 y 3: navegar solo tras confirmar el guardado en Room.
                        Mientras se guarda, bloquear otro envío; ante error, mantener
                        la cotización abierta con sus datos y permitir reintentar.
                 */
                { comercio.solicitar(it) },
                //revisar end
                { navController.popBackStack() },
                {
                    // Tras la animación de confirmación se abre el historial de solicitudes.
                    navController.navigate("solicitudes") {
                        popUpTo("cotizar") { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }
        composable("solicitudes") {
            if (usuario != null) SolicitudesScreen(
                solicitudes,
                usuario!!.rol == "ADMIN",
                { id, estado -> comercio.cambiarEstado(id, estado) },
                { navController.popBackStack() },
                productos)
        }
        composable("productos_admin") {
            if (usuario?.rol == "ADMIN") ProductosAdminScreen(
                productos = productos,
                guardar = { comercio.guardarProducto(it) },
                eliminar = { comercio.eliminarProducto(it) },
                volver = { navController.popBackStack() }
            )
        }
        composable("campanas_admin") {
            if (usuario?.rol == "ADMIN") CampanasScreen(
                campanas,
                true,
                { comercio.guardarCampana(it) },
                { navController.popBackStack() })
        }
        composable("novedades") {
            if (usuario != null) NotificacionesScreen(
                campanas,
                { navController.popBackStack() })
        }
        composable("mapa") { if (usuario != null) MapaFeriasScreen { navController.popBackStack() } }
        composable("qr") {
            if (usuario != null) EscanerQRScreen({ id ->
                productoSeleccionado = id; ir("detalle")
            }, { navController.popBackStack() })
        }
    }
    }
    //revisar end
    // La confirmación de envío de una cotización ya se muestra con su propia animación.
    LaunchedEffect(mensaje) {
        if (mensaje == "Solicitud enviada correctamente") comercio.limpiarMensaje()
    }
    if (mensaje.isNotBlank() && mensaje != "Solicitud enviada correctamente") AlertDialog(
        onDismissRequest = { comercio.limpiarMensaje() },
        title = { Text("Lou Araya Macramé") },
        text = { Text(mensaje) },
        confirmButton = { TextButton(onClick = { comercio.limpiarMensaje() }) { Text("Aceptar") } }
    )
}
