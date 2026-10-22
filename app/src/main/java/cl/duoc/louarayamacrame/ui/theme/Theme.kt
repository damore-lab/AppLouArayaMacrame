package cl.duoc.louarayamacrame.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresMacrame = lightColorScheme(
    primary = Color(0xFF8D5A43),
    onPrimary = Color.White,

    secondary = Color(0xFF795548),
    onSecondary = Color.White,

    background = Color(0xFFFFF8F0),
    onBackground = Color(0xFF4E342E),

    surface = Color(0xFFFFF8F0),
    onSurface = Color(0xFF4E342E),

    onSurfaceVariant = Color(0xFF5D4037),

    outline = Color(0xFF795548),

    error = Color(0xFFB42B22),
    onError = Color.White
)

@Composable
fun AppMacraméTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresMacrame,
        content = content
    )
}
