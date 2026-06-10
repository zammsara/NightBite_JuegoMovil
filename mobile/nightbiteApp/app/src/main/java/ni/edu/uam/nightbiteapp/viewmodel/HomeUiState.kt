package ni.edu.uam.nightbiteapp.viewmodel

import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.model.NightLevel

data class HomeUiState(
    val isLoading: Boolean = false,
    val user: UserResponse? = null,
    val levels: List<NightLevel> = emptyList(),
    val errorMessage: String? = null,

    /**
     * Controla la visualización del diálogo de confirmación
     * previo a eliminar la cuenta.
     */
    val showDeleteAccountDialog: Boolean = false,

    /**
     * Controla la visualización del mensaje final
     * cuando la cuenta fue eliminada correctamente.
     */
    val showAccountDeletedDialog: Boolean = false

) {
    val hasPlayer: Boolean
        get() = user?.player != null
}