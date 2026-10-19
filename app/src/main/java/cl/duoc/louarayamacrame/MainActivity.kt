package cl.duoc.louarayamacrame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cl.duoc.louarayamacrame.navigation.AppNavigation
import cl.duoc.louarayamacrame.ui.theme.AppMacraméTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppMacraméTheme {
                AppNavigation()
            }
        }
    }
}
