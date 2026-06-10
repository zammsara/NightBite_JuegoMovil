package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData
import ni.edu.uam.nightbiteapp.data.repository.UserRepository
import ni.edu.uam.nightbiteapp.data.local.mock.NightProgressData

class HomeViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            levels = NightLevelsData.levels
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadHomeData(userId: Long?) {
        val maxUnlockedLevelId = NightProgressData.getMaxUnlockedLevel(userId)
        val levelsByProgress = NightLevelsData.getLevelsByProgress(maxUnlockedLevelId)

        if (userId == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    user = null,
                    levels = levelsByProgress,
                    errorMessage = "No hay una sesión activa."
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null,
                        levels = levelsByProgress
                    )
                }

                val response = userRepository.getUserById(userId)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = response.body(),
                            levels = levelsByProgress,
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = null,
                            levels = levelsByProgress,
                            errorMessage = "No se pudo cargar la información del usuario."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = null,
                        levels = levelsByProgress,
                        errorMessage = "Error de conexión con la API."
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    /**
     * Solicita mostrar el diálogo de confirmación
     * para eliminar la cuenta del usuario.
     */
    fun showDeleteAccountDialog() {
        _uiState.update {
            it.copy(
                showDeleteAccountDialog = true
            )
        }
    }

    /**
     * Oculta el diálogo de confirmación
     * de eliminación de cuenta.
     */
    fun dismissDeleteAccountDialog() {
        _uiState.update {
            it.copy(
                showDeleteAccountDialog = false
            )
        }
    }

    /**
     * Oculta el mensaje mostrado después
     * de eliminar la cuenta correctamente.
     */
    fun dismissAccountDeletedDialog() {
        _uiState.update {
            it.copy(
                showAccountDeletedDialog = false
            )
        }
    }

    /**
     * Elimina la cuenta actualmente cargada
     * en el HomeUiState.
     */
    fun deleteAccount() {

        val userId = _uiState.value.user?.id ?: return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    showDeleteAccountDialog = false,
                    errorMessage = null
                )
            }

            try {

                val response = userRepository.deleteUser(userId)

                if (response.isSuccessful) {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showAccountDeletedDialog = true
                        )
                    }

                } else {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se pudo eliminar la cuenta."
                        )
                    }

                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error de conexión con la API."
                    )
                }

            }
        }
    }
}