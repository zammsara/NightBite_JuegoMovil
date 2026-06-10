package ni.edu.uam.nightbiteapp.viewmodel

/**
 * Estado de interfaz utilizado durante el flujo
 * de eliminación de cuenta.
 */
data class DeleteAccountUiState(

    /**
     * Indica si debe mostrarse el diálogo
     * de confirmación de eliminación.
     */
    val showDeleteConfirmationDialog: Boolean = false,

    /**
     * Indica si debe mostrarse el diálogo
     * informando que la cuenta fue eliminada
     * correctamente.
     */
    val showDeleteSuccessDialog: Boolean = false,

    /**
     * Indica si se está ejecutando una operación
     * de eliminación contra la API.
     */
    val isLoading: Boolean = false,

    /**
     * Mensaje de error a mostrar cuando ocurre
     * un problema durante la eliminación.
     */
    val errorMessage: String? = null
)