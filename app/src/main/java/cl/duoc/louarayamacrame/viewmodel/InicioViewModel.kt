package cl.duoc.louarayamacrame.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/* revisar start
        Punto 22: comprobar si este ViewModel sigue sin referencias. Si no participa
        en bienvenida o dashboard, retirarlo al reorganizar; si se reutiliza, asignarle
        una responsabilidad concreta y conectarlo a la pantalla correspondiente.
 */
class InicioViewModel : ViewModel() {

    private val _mensaje = MutableStateFlow(
        "Bienvenido a Lou Araya Macramé"
    )

    val mensaje = _mensaje.asStateFlow()

    fun cambiarMensaje() {
        _mensaje.value =
            "Descubre nuestros productos artesanales"
    }
}
//revisar end
