package ni.edu.uam.nightbiteapp.viewmodel

data class PlayerCreationUiState(
    val nickname: String = "",
    val driverName: String = "",

    val nicknameError: String? = null,
    val driverNameError: String? = null,

    val gender: String = "",
    val helmetColor: String = "",
    val motorcycleType: String = "",

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isPlayerCreated: Boolean = false
)