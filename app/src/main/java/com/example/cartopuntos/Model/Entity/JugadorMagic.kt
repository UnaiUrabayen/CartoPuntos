data class JugadorMagic(
    val id: Int,
    val nombre: String,
    var vida: Int,
    val vidaInicial: Int = 40,

    // Contadores adicionales
    var contadorVeneno: Int = 0,
    var contadorTesoros: Int = 0,
    var contadorEnergia: Int = 0,
    var contadorMasUno: Int = 0, // +1/+1
    var esMonarca: Boolean = false,
    var tieneIniciativa: Boolean = false,

    // Estado de día o noche
    var esDia: Boolean = true,

    // Comandante (daño recibido de otros jugadores)
    val dañoDeComandantes: MutableMap<Int, Int> = mutableMapOf() // clave: ID del comandante enemigo

) {
    fun actualizarEstadoDeMuerte() {
        if (contadorVeneno >= 10) {
            vida = 0
            return
        }

        for (daño in dañoDeComandantes.values) {
            if (daño > 21) {
                vida = 0
                break
            }
        }
    }
}
