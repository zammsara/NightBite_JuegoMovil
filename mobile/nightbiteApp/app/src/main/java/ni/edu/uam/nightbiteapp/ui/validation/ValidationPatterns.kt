package ni.edu.uam.nightbiteapp.ui.validation

/**
 * Contiene todas las expresiones regulares utilizadas
 * por el sistema de validaciones.
 *
 * Mantenerlas centralizadas facilita su reutilización
 * y mantenimiento.
 */
object ValidationPatterns {

    /**
     * Username:
     * minúsculas, números y guion bajo.
     */
    val USERNAME_REGEX =
        Regex("^[a-z0-9_]+$")

    /**
     * Nickname:
     * letras, números y guion bajo.
     */
    val NICKNAME_REGEX =
        Regex("^[a-zA-Z0-9_]+$")

    /**
     * Nombre real del repartidor.
     *
     * Permite:
     * - letras
     * - espacios
     * - caracteres acentuados
     */
    val DRIVER_NAME_REGEX =
        Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")
}