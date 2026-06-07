package ni.edu.uam.nightbiteapp.ui.validation

import android.util.Patterns

/**
 * Punto central de validaciones del proyecto.
 *
 * Todas las pantallas deben utilizar estas funciones
 * para mantener reglas consistentes en toda la aplicación.
 *
 * Convención:
 *
 * - null -> válido
 * - String -> mensaje de error
 */
object Validators {

    // =====================================================
    // REGISTER
    // =====================================================

    fun validateUsername(
        username: String
    ): String? {

        if (username.isBlank()) {
            return "El nombre de usuario es obligatorio."
        }

        if (
            username.length <
            ValidationConstants.USERNAME_MIN_LENGTH
        ) {
            return "El nombre de usuario debe tener al menos 3 caracteres."
        }

        if (
            username.length >
            ValidationConstants.USERNAME_MAX_LENGTH
        ) {
            return "El nombre de usuario no puede tener más de 16 caracteres."
        }

        if (
            !username.matches(
                ValidationPatterns.USERNAME_REGEX
            )
        ) {
            return "Solo se permiten minúsculas, números y guion bajo (_)."
        }

        return null
    }

    fun validateEmail(
        email: String
    ): String? {

        if (email.isBlank()) {
            return "El correo electrónico es obligatorio."
        }

        if (
            !Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            return "Ingresa un correo electrónico válido."
        }

        return null
    }

    fun validatePassword(
        password: String
    ): String? {

        if (password.isBlank()) {
            return "La contraseña es obligatoria."
        }

        if (
            password.length <
            ValidationConstants.PASSWORD_MIN_LENGTH
        ) {
            return "La contraseña debe tener al menos 8 caracteres."
        }

        if (
            password.length >
            ValidationConstants.PASSWORD_MAX_LENGTH
        ) {
            return "La contraseña no puede superar los 64 caracteres."
        }

        if (!password.any { it.isUpperCase() }) {
            return "La contraseña debe contener al menos una letra mayúscula."
        }

        if (!password.any { it.isLowerCase() }) {
            return "La contraseña debe contener al menos una letra minúscula."
        }

        if (!password.any { it.isDigit() }) {
            return "La contraseña debe contener al menos un número."
        }

        return null
    }

    fun validateConfirmPassword(
        password: String,
        confirmPassword: String
    ): String? {

        if (confirmPassword.isBlank()) {
            return "Confirma tu contraseña."
        }

        if (password != confirmPassword) {
            return "Las contraseñas no coinciden."
        }

        return null
    }

    // =====================================================
    // PLAYER
    // =====================================================

    fun validateNickname(
        nickname: String
    ): String? {

        if (nickname.isBlank()) {
            return "El apodo es obligatorio."
        }

        if (
            nickname.length <
            ValidationConstants.NICKNAME_MIN_LENGTH
        ) {
            return "El apodo debe tener al menos 3 caracteres."
        }

        if (
            nickname.length >
            ValidationConstants.NICKNAME_MAX_LENGTH
        ) {
            return "El apodo no puede superar los 16 caracteres."
        }

        if (
            !nickname.matches(
                ValidationPatterns.NICKNAME_REGEX
            )
        ) {
            return "Solo se permiten letras, números y guion bajo (_)."
        }

        return null
    }

    fun validateDriverName(
        driverName: String
    ): String? {

        if (driverName.isBlank()) {
            return "El nombre del repartidor es obligatorio."
        }

        if (
            driverName.length <
            ValidationConstants.DRIVER_NAME_MIN_LENGTH
        ) {
            return "El nombre debe tener al menos 2 caracteres."
        }

        if (
            driverName.length >
            ValidationConstants.DRIVER_NAME_MAX_LENGTH
        ) {
            return "El nombre no puede superar los 50 caracteres."
        }

        if (
            !driverName.matches(
                ValidationPatterns.DRIVER_NAME_REGEX
            )
        ) {
            return "Solo se permiten letras y espacios."
        }

        return null
    }
}