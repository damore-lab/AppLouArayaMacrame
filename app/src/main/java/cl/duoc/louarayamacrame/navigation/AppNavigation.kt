package cl.duoc.louarayamacrame.navigation

import cl.duoc.louarayamacrame.viewmodel.PerfilViewModel
import cl.duoc.louarayamacrame.ui.screens.PerfilScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.louarayamacrame.ui.screens.InicioScreen
import cl.duoc.louarayamacrame.ui.screens.LoginScreen
import cl.duoc.louarayamacrame.ui.screens.RegistroScreen
import cl.duoc.louarayamacrame.viewmodel.LoginViewModel
import cl.duoc.louarayamacrame.viewmodel.RegistroViewModel
import androidx.compose.ui.platform.LocalContext
import cl.duoc.louarayamacrame.data.AppDatabase
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.louarayamacrame.data.UsuarioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    var usuarioAutenticadoId by remember {
        mutableStateOf<Int?>(null)
    }

    val context = LocalContext.current
    val database = androidx.compose.runtime.remember(context) {
        AppDatabase.obtenerBaseDatos(context)
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ) {
        composable(Rutas.BIENVENIDA) {
            InicioScreen(
                onLogin = {
                    navController.navigate(Rutas.LOGIN)
                },
                onRegistro = {
                    navController.navigate(Rutas.REGISTRO)
                }
            )
        }

        composable(Rutas.LOGIN) {
            val vm: LoginViewModel = viewModel()

            val usuarioId by vm.usuarioId.collectAsStateWithLifecycle()

            LaunchedEffect(vm, database) {
                vm.configurarDao(database.usuarioDao())
            }

            LoginScreen(
                viewModel = vm,
                onRegistro = {
                    navController.navigate(Rutas.REGISTRO)
                },
                onVolver = {
                    navController.popBackStack()
                },
                onLoginExitoso = {
                    val id = usuarioId

                    if (id != null) {
                        usuarioAutenticadoId = id

                        navController.navigate(Rutas.PERFIL) {
                            popUpTo(Rutas.LOGIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Rutas.REGISTRO) {
            val vm: RegistroViewModel = viewModel()

            androidx.compose.runtime.LaunchedEffect(vm, database) {
                vm.configurarDao(database.usuarioDao())
            }

            RegistroScreen(
                viewModel = vm,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }



        composable(Rutas.PERFIL) {

            val perfilViewModel: PerfilViewModel = viewModel()
            val id = usuarioAutenticadoId

            var usuario by remember(id) {
                mutableStateOf<UsuarioEntity?>(null)
            }

            LaunchedEffect(id) {
                if (id == null) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.PERFIL) {
                            inclusive = true
                        }
                    }
                } else {
                    usuario = withContext(Dispatchers.IO) {
                        database.usuarioDao().buscarPorId(id)
                    }

                    if (usuario == null) {
                        usuarioAutenticadoId = null

                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.PERFIL) {
                                inclusive = true
                            }
                        }
                    }
                }
            }


            usuario?.let { usuarioActual ->
                PerfilScreen(
                    usuario = usuarioActual,
                    viewModel = perfilViewModel,
                    onGuardar = { nombreNuevo, aliasNuevo ->
                        perfilViewModel.actualizarPerfil(
                            usuario = usuarioActual,
                            nombreNuevo = nombreNuevo,
                            aliasNuevo = aliasNuevo,
                            dao = database.usuarioDao(),
                            onGuardado = { usuarioActualizado ->
                                usuario = usuarioActualizado
                            }
                        )
                    },
                    onCerrarSesion = {
                        usuarioAutenticadoId = null

                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.BIENVENIDA) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
