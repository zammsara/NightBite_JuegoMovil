package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.repository.UserRepository

/**
 * ViewModel encargado del flujo de eliminación
 * de cuenta de usuario.
 */
class DeleteAccountViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DeleteAccountUiState()
    )

    val uiState: StateFlow<DeleteAccountUiState> =
        _uiState.asStateFlow()

    /**
     * Muestra el diálogo de confirmación.
     */
    fun showDeleteConfirmationDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteConfirmationDialog = true
        )
    }

    /**
     * Cierra el diálogo de confirmación.
     */
    fun dismissDeleteConfirmationDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteConfirmationDialog = false
        )
    }

    /**
     * Ejecuta la eliminación de la cuenta.
     */
    fun deleteAccount(userId: Long?) {

        if (userId == null) {
            _uiState.value = _uiState.value.copy(
                showDeleteConfirmationDialog = false,
                errorMessage = "No se pudo identificar al usuario activo."
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                showDeleteConfirmationDialog = false,
                errorMessage = null
            )

            try {

                val response = userRepository.deleteUser(userId)

                if (!response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo eliminar la cuenta."
                    )

                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    showDeleteSuccessDialog = true
                )

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error de conexión con el servidor."
                )
            }
        }
    }

    /**
     * Cierra el diálogo de éxito.
     */
    fun dismissDeleteSuccessDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteSuccessDialog = false
        )
    }

    /**
     * Limpia la sesión local una vez que la cuenta
     * fue eliminada correctamente.
     */
    fun clearSessionAndFinish(
        onSessionCleared: () -> Unit
    ) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onSessionCleared()
        }
    }

    /**
     * Limpia el mensaje de error actual.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}