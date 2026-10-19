package cl.duoc.louarayamacrame.navigation

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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

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

            LoginScreen(
                viewModel = vm,
                onRegistro = {
                    navController.navigate(Rutas.REGISTRO)
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.REGISTRO) {
            val vm: RegistroViewModel = viewModel()

            RegistroScreen(
                viewModel = vm,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}
