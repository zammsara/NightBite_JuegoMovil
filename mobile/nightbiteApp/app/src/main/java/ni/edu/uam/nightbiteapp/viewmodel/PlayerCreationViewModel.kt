package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerRequest
import ni.edu.uam.nightbiteapp.data.repository.PlayerRepository
import ni.edu.uam.nightbiteapp.ui.validation.Validators

class PlayerCreationViewModel(
    private val sessionManager: SessionManager,
    private val playerRepository: PlayerRepository = PlayerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerCreationUiState())
    val uiState: StateFlow<PlayerCreationUiState> = _uiState

    val genderOptions = listOf("Femenino", "Masculino")
    val helmetColorOptions = listOf("Negro", "Rojo", "Azul", "Blanco", "Amarillo")
    val motorcycleTypeOptions = listOf("Estándar", "Scooter", "Deportiva", "Retro", "Delivery")

    fun onNicknameChange(value: String) {
        val validationError =
            Validators.validateNickname(value)

        _uiState.update {
            it.copy(
                nickname = value,
                nicknameError = validationError,
                errorMessage = null
            )
        }
    }

    fun onDriverNameChange(value: String) {
        val validationError =
            Validators.validateDriverName(value)

        _uiState.update {
            it.copy(
                driverName = value,
                driverNameError = validationError,
                errorMessage = null
            )
        }
    }

    fun onGenderSelected(value: String) {
        _uiState.update {
            it.copy(
                gender = value,
                genderError = null,
                errorMessage = null
            )
        }
    }

    fun onHelmetColorSelected(value: String) {
        _uiState.update {
            it.copy(
                helmetColor = value,
                helmetColorError = null,
                errorMessage = null
            )
        }
    }

    fun onMotorcycleTypeSelected(value: String) {
        _uiState.update {
            it.copy(
                motorcycleType = value,
                motorcycleTypeError = null,
                errorMessage = null
            )
        }
    }

    fun createPlayer() {
        val currentState = _uiState.value

        val nicknameError =
            Validators.validateNickname(
                currentState.nickname
            )

        val driverNameError =
            Validators.validateDriverName(
                currentState.driverName
            )

        if (
            nicknameError != null ||
            driverNameError != null
        ) {

            _uiState.update {
                it.copy(
                    nicknameError = nicknameError,
                    driverNameError = driverNameError
                )
            }

            return
        }

        val genderError =
            if (currentState.gender.isBlank()) {
                "Selecciona el género."
            } else {
                null
            }

        val helmetColorError =
            if (currentState.helmetColor.isBlank()) {
                "Selecciona el color del casco."
            } else {
                null
            }

        val motorcycleTypeError =
            if (currentState.motorcycleType.isBlank()) {
                "Selecciona el tipo de moto."
            } else {
                null
            }

        if (
            genderError != null ||
            helmetColorError != null ||
            motorcycleTypeError != null
        ) {

            _uiState.update {
                it.copy(
                    genderError = genderError,
                    helmetColorError = helmetColorError,
                    motorcycleTypeError = motorcycleTypeError
                )
            }

            return
        }

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }

                val session = sessionManager.userSessionFlow.first()
                val userId = session.userId

                if (userId == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se encontró una sesión activa. Inicia sesión nuevamente."
                        )
                    }
                    return@launch
                }

                val playerRequest = PlayerRequest(
                    userAccountId = userId,
                    nickname = currentState.nickname.trim(),
                    driverName = currentState.driverName.trim(),
                    gender = currentState.gender,
                    helmetColor = currentState.helmetColor,
                    motorcycleType = currentState.motorcycleType
                )

                val response = playerRepository.createPlayer(playerRequest)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isPlayerCreated = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se pudo crear la ficha. Verifica si este usuario ya tiene un Player."
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

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(errorMessage = message)
        }
    }
}