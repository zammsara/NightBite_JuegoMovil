package ni.edu.uam.nightbiteapp.ui.validation

/**
 * Centraliza todas las constantes utilizadas por las validaciones.
 *
 * Evita el uso de números mágicos dentro de los validadores
 * y facilita modificar reglas de negocio futuras.
 */
object ValidationConstants {

    // Username
    const val USERNAME_MIN_LENGTH = 3
    const val USERNAME_MAX_LENGTH = 16

    // Password
    const val PASSWORD_MIN_LENGTH = 8
    const val PASSWORD_MAX_LENGTH = 64

    // Nickname del Player
    const val NICKNAME_MIN_LENGTH = 3
    const val NICKNAME_MAX_LENGTH = 16

    // Nombre del repartidor
    const val DRIVER_NAME_MIN_LENGTH = 3
    const val DRIVER_NAME_MAX_LENGTH = 50
}