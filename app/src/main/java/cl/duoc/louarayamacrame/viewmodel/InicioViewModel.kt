package cl.duoc.louarayamacrame.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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