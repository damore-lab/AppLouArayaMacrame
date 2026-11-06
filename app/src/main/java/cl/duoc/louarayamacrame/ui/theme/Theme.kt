package cl.duoc.louarayamacrame.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ColoresMacrame = lightColorScheme(
    primary = Terracota,
    onPrimary = Color.White,
    primaryContainer = TerracotaClaro,
    onPrimaryContainer = CafeProfundo,

    secondary = CafeSuave,
    onSecondary = Color.White,
    secondaryContainer = Arena,
    onSecondaryContainer = CafeProfundo,

    tertiary = Oliva,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDDE8CF),
    onTertiaryContainer = Color(0xFF243318),

    background = Crema,
    onBackground = CafeProfundo,

    surface = Crema,
    onSurface = CafeProfundo,
    surfaceVariant = Arena,
    onSurfaceVariant = CafeSuave,

    surfaceContainerLowest = Color.White,
    surfaceContainerLow = ArenaClara,
    surfaceContainer = Color(0xFFF7E9DB),
    surfaceContainerHigh = Arena,
    surfaceContainerHighest = Color(0xFFEBD8C6),

    outline = Color(0xFF8D6E63),
    outlineVariant = Color(0xFFE2CDBB),

    error = Color(0xFFB42B22),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDA),
    onErrorContainer = Color(0xFF410E0B)
)

private val FormasMacrame = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun AppMacraméTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // La identidad visual de Lou Araya Macramé usa siempre la paleta clara.
    MaterialTheme(
        colorScheme = ColoresMacrame,
        typography = Typography,
        shapes = FormasMacrame,
        content = content
    )
}
