package cl.duoc.louarayamacrame.navigation

import androidx.compose.runtime.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
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
    var usuarioAutenticadoId by remember { mutableStateOf<Int?>(null) }
    var usuario by remember { mutableStateOf<UsuarioEntity?>(null) }
    var productoSeleccionado by remember { mutableIntStateOf(0) }
    val comercio: ComercioViewModel = viewModel()
    val productos by comercio.productos.collectAsStateWithLifecycle()
    val favoritos by comercio.favoritos.collectAsStateWithLifecycle()
    val solicitudes by comercio.solicitudes.collectAsStateWithLifecycle()
    val campanas by comercio.campanas.collectAsStateWithLifecycle()
    val mensaje by comercio.mensaje.collectAsStateWithLifecycle()

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
    fun ir(ruta: String) {
        navController.navigate(ruta) { launchSingleTop = true }
    }

    fun salir() {
        usuarioAutenticadoId = null
        navController.navigate(Rutas.LOGIN) {
            popUpTo(Rutas.BIENVENIDA) { inclusive = false }
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = Rutas.BIENVENIDA) {
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
            } else DashboardScreen(actual.nombre, actual.rol == "ADMIN") { opcion ->
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
            }
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
                onCerrarSesion = { salir() })
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
                { navController.popBackStack() })
        }
        composable("cotizar") {
            if (usuario != null && usuario!!.rol != "ADMIN") CotizacionScreen(
                usuario!!.id,
                productoSeleccionado.takeIf { it > 0 },
                { comercio.solicitar(it); ir("solicitudes") },
                { navController.popBackStack() })
        }
        composable("solicitudes") {
            if (usuario != null) SolicitudesScreen(
                solicitudes,
                usuario!!.rol == "ADMIN",
                { id, estado -> comercio.cambiarEstado(id, estado) },
                { navController.popBackStack() })
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
            if (usuario != null) CampanasScreen(
                campanas,
                false,
                {},
                { navController.popBackStack() })
        }
        composable("mapa") { if (usuario != null) MapaFeriasScreen { navController.popBackStack() } }
        composable("qr") {
            if (usuario != null) EscanerQRScreen({ id ->
                productoSeleccionado = id; ir("detalle")
            }, { navController.popBackStack() })
        }
    }
    if (mensaje.isNotBlank()) AlertDialog(
        onDismissRequest = { comercio.limpiarMensaje() },
        title = { Text("Lou Araya Macramé") },
        text = { Text(mensaje) },
        confirmButton = { TextButton(onClick = { comercio.limpiarMensaje() }) { Text("Aceptar") } }
    )
}
