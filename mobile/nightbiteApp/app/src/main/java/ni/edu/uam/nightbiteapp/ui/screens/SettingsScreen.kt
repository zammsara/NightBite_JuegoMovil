package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog

data class AccountFormData(
    val username: String,
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String,
    val nickname: String,
    val driverName: String,
    val gender: String,
    val helmetColor: String,
    val motorcycleType: String
)

private data class AccountValidationErrors(
    val usernameError: String? = null,
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val nicknameError: String? = null,
    val driverNameError: String? = null,
    val genderError: String? = null,
    val helmetColorError: String? = null,
    val motorcycleTypeError: String? = null
)

@Composable
fun SettingsScreen(
    userSession: UserSession,
    onNavigateToAccount: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    onBackToHome: () -> Unit,
    onSaveAccountChanges: (AccountFormData) -> Unit = {}
) {
    var musicEnabled by remember {
        mutableStateOf(true)
    }

    var soundEnabled by remember {
        mutableStateOf(true)
    }

    var vibrationEnabled by remember {
        mutableStateOf(true)
    }

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var showAccountEditor by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .navigationBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        AccountSummaryCard(
            userSession = userSession,
            onNavigateToAccount = {
                showAccountEditor = !showAccountEditor
            },
            onLogoutClick = {
                showLogoutDialog = true
            },
            onDeleteAccountClick = {
                showDeleteDialog = true
            }
        )

        if (showAccountEditor) {
            Spacer(modifier = Modifier.height(20.dp))

            AccountEditCard(
                userSession = userSession,
                onCancel = {
                    showAccountEditor = false
                },
                onSaveAccountChanges = onSaveAccountChanges
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        GameOptionsCard(
            musicEnabled = musicEnabled,
            onMusicChange = {
                musicEnabled = it
            },
            soundEnabled = soundEnabled,
            onSoundChange = {
                soundEnabled = it
            },
            vibrationEnabled = vibrationEnabled,
            onVibrationChange = {
                vibrationEnabled = it
            },
            notificationsEnabled = notificationsEnabled,
            onNotificationsChange = {
                notificationsEnabled = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver al menú principal")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showLogoutDialog) {
        NightMessageDialog(
            title = "Cerrar sesión",
            message = "¿Deseas cerrar tu sesión y volver al inicio de sesión?",
            confirmText = "Confirmar",
            dismissText = "Cancelar",
            icon = Icons.Default.Logout,
            iconColor = MaterialTheme.colorScheme.error,
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        NightMessageDialog(
            title = "Eliminar cuenta",
            message = "¿Deseas eliminar tu cuenta? Esta acción no se puede deshacer.",
            confirmText = "Eliminar",
            dismissText = "Cancelar",
            icon = Icons.Default.Logout,
            iconColor = MaterialTheme.colorScheme.error,
            onConfirm = {
                showDeleteDialog = false
                onDeleteAccount()
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}

@Composable
private fun AccountSummaryCard(
    userSession: UserSession,
    onNavigateToAccount: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 52.dp)
                ) {
                    Text(
                        text = "Datos de la cuenta",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Usuario: ${userSession.username}")
                    Text(text = "Correo: ${userSession.email}")
                }

                IconButton(
                    onClick = onNavigateToAccount,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Actualizar credenciales"
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val buttonWidth = (maxWidth - 12.dp) / 2

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onLogoutClick,
                        modifier = Modifier.width(buttonWidth),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(text = "Cerrar sesión")
                    }

                    OutlinedButton(
                        onClick = onDeleteAccountClick,
                        modifier = Modifier.width(buttonWidth),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = "Eliminar cuenta")
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountEditCard(
    userSession: UserSession,
    onCancel: () -> Unit,
    onSaveAccountChanges: (AccountFormData) -> Unit
) {
    var username by remember {
        mutableStateOf(userSession.username)
    }

    var currentPassword by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var nickname by remember {
        mutableStateOf("")
    }

    var driverName by remember {
        mutableStateOf("")
    }

    var selectedGender by remember {
        mutableStateOf("")
    }

    var selectedHelmetColor by remember {
        mutableStateOf("")
    }

    var selectedMotorcycleType by remember {
        mutableStateOf("")
    }

    var errors by remember {
        mutableStateOf(AccountValidationErrors())
    }

    var successMessage by remember {
        mutableStateOf("")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = "Actualizar datos",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = formatUsernameInput(it)
                    successMessage = ""
                },
                label = {
                    Text(text = "Nombre de usuario")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = errors.usernameError != null,
                supportingText = {
                    errors.usernameError?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                    successMessage = ""
                },
                label = {
                    Text(text = "Contraseña actual")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = errors.currentPasswordError != null,
                supportingText = {
                    errors.currentPasswordError?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    successMessage = ""
                },
                label = {
                    Text(text = "Nueva contraseña")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = errors.newPasswordError != null,
                supportingText = {
                    errors.newPasswordError?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    successMessage = ""
                },
                label = {
                    Text(text = "Confirmar contraseña")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = errors.confirmPasswordError != null,
                supportingText = {
                    errors.confirmPasswordError?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nickname,
                onValueChange = {
                    nickname = formatNicknameInput(it)
                    successMessage = ""
                },
                label = {
                    Text(text = "Apodo")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = errors.nicknameError != null,
                supportingText = {
                    errors.nicknameError?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = driverName,
                onValueChange = {
                    driverName = formatDriverNameInput(it)
                    successMessage = ""
                },
                label = {
                    Text(text = "Nombre del repartidor")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = errors.driverNameError != null,
                supportingText = {
                    errors.driverNameError?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OptionSelector(
                title = "Género",
                options = listOf("Masculino", "Femenino", "Otro"),
                selectedOption = selectedGender,
                error = errors.genderError,
                onOptionSelected = {
                    selectedGender = it
                    successMessage = ""
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            OptionSelector(
                title = "Color de casco",
                options = listOf("Rojo", "Azul", "Negro", "Blanco", "Verde", "Amarillo"),
                selectedOption = selectedHelmetColor,
                error = errors.helmetColorError,
                onOptionSelected = {
                    selectedHelmetColor = it
                    successMessage = ""
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            OptionSelector(
                title = "Tipo de moto",
                options = listOf("Scooter", "Deportiva", "Clásica", "Doble propósito"),
                selectedOption = selectedMotorcycleType,
                error = errors.motorcycleTypeError,
                onOptionSelected = {
                    selectedMotorcycleType = it
                    successMessage = ""
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (successMessage.isNotBlank()) {
                Text(
                    text = successMessage,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Cancelar")
                }

                Button(
                    onClick = {
                        val validationResult = validateAccountFields(
                            username = username.trim(),
                            currentUsername = userSession.username,
                            currentPassword = currentPassword,
                            newPassword = newPassword,
                            confirmPassword = confirmPassword,
                            nickname = nickname.trim(),
                            driverName = driverName.trim(),
                            gender = selectedGender,
                            helmetColor = selectedHelmetColor,
                            motorcycleType = selectedMotorcycleType
                        )

                        errors = validationResult

                        if (!validationResult.hasErrors()) {
                            onSaveAccountChanges(
                                AccountFormData(
                                    username = username.trim(),
                                    currentPassword = currentPassword,
                                    newPassword = newPassword,
                                    confirmPassword = confirmPassword,
                                    nickname = nickname.trim(),
                                    driverName = driverName.trim(),
                                    gender = selectedGender,
                                    helmetColor = selectedHelmetColor,
                                    motorcycleType = selectedMotorcycleType
                                )
                            )

                            successMessage = "Datos validados correctamente."
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Guardar")
                }
            }
        }
    }
}

@Composable
private fun OptionSelector(
    title: String,
    options: List<String>,
    selectedOption: String,
    error: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        options.chunked(2).forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowOptions.forEach { option ->
                    if (selectedOption == option) {
                        Button(
                            onClick = {
                                onOptionSelected(option)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = option)
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                onOptionSelected(option)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = option)
                        }
                    }
                }

                if (rowOptions.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun GameOptionsCard(
    musicEnabled: Boolean,
    onMusicChange: (Boolean) -> Unit,
    soundEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    vibrationEnabled: Boolean,
    onVibrationChange: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = "Opciones del juego",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val columnWidth = (maxWidth - 24.dp) / 2

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier.width(columnWidth)
                    ) {
                        SettingSwitchItem(
                            title = "Música",
                            checked = musicEnabled,
                            onCheckedChange = onMusicChange
                        )

                        SettingSwitchItem(
                            title = "Sonido",
                            checked = soundEnabled,
                            onCheckedChange = onSoundChange
                        )
                    }

                    Column(
                        modifier = Modifier.width(columnWidth)
                    ) {
                        SettingSwitchItem(
                            title = "Vibración",
                            checked = vibrationEnabled,
                            onCheckedChange = onVibrationChange
                        )

                        SettingSwitchItem(
                            title = "Notificaciones",
                            checked = notificationsEnabled,
                            onCheckedChange = onNotificationsChange
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingSwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

private fun validateAccountFields(
    username: String,
    currentUsername: String,
    currentPassword: String,
    newPassword: String,
    confirmPassword: String,
    nickname: String,
    driverName: String,
    gender: String,
    helmetColor: String,
    motorcycleType: String
): AccountValidationErrors {
    val usernameRegex = Regex("^[a-z0-9_]{1,16}$")
    val nicknameRegex = Regex("^[A-Za-zÁÉÍÓÚÑáéíóúñ0-9 ]{1,20}$")
    val driverNameRegex = Regex("^[A-ZÁÉÍÓÚÑ][a-záéíóúñ]*(\\s[A-ZÁÉÍÓÚÑ][a-záéíóúñ]*)*$")

    return AccountValidationErrors(
        usernameError = when {
            username.isBlank() -> "El nombre de usuario es obligatorio."
            username.contains(" ") -> "El usuario no puede llevar espacios."
            username.length > 16 -> "El usuario no puede tener más de 16 caracteres."
            !usernameRegex.matches(username) -> "Solo se permiten letras minúsculas, números y guion bajo."
            username == currentUsername -> "El nuevo usuario debe ser diferente al usuario actual."
            else -> null
        },

        currentPasswordError = when {
            currentPassword.isBlank() -> "La contraseña actual es obligatoria."
            else -> null
        },

        newPasswordError = when {
            newPassword.isBlank() -> "La nueva contraseña es obligatoria."
            newPassword.length < 8 -> "La nueva contraseña debe tener mínimo 8 caracteres."
            newPassword == currentPassword -> "La nueva contraseña debe ser diferente de la actual."
            else -> null
        },

        confirmPasswordError = when {
            confirmPassword.isBlank() -> "La confirmación de contraseña es obligatoria."
            confirmPassword != newPassword -> "La nueva contraseña y la confirmación deben coincidir."
            else -> null
        },

        nicknameError = when {
            nickname.isBlank() -> "El apodo es obligatorio."
            nickname.length > 20 -> "El apodo no puede tener más de 20 caracteres."
            !nicknameRegex.matches(nickname) -> "El apodo solo puede llevar letras, números y espacios."
            else -> null
        },

        driverNameError = when {
            driverName.isBlank() -> "El nombre del repartidor es obligatorio."
            driverName.length > 30 -> "El nombre no puede tener más de 30 caracteres."
            !driverNameRegex.matches(driverName) -> "Solo letras y espacios. Usa mayúscula solo al inicio de cada nombre."
            else -> null
        },

        genderError = when {
            gender.isBlank() -> "Debes seleccionar un género."
            else -> null
        },

        helmetColorError = when {
            helmetColor.isBlank() -> "Debes seleccionar un color de casco."
            else -> null
        },

        motorcycleTypeError = when {
            motorcycleType.isBlank() -> "Debes seleccionar un tipo de moto."
            else -> null
        }
    )
}

private fun AccountValidationErrors.hasErrors(): Boolean {
    return usernameError != null ||
            currentPasswordError != null ||
            newPasswordError != null ||
            confirmPasswordError != null ||
            nicknameError != null ||
            driverNameError != null ||
            genderError != null ||
            helmetColorError != null ||
            motorcycleTypeError != null
}

private fun formatUsernameInput(value: String): String {
    return value
        .lowercase()
        .filter {
            it in 'a'..'z' || it.isDigit() || it == '_'
        }
        .take(16)
}

private fun formatNicknameInput(value: String): String {
    return value
        .filter {
            it.isLetterOrDigit() || it == ' '
        }
        .replace(Regex("\\s+"), " ")
        .take(20)
}

private fun formatDriverNameInput(value: String): String {
    val cleanValue = value
        .filter {
            it.isLetter() || it == ' '
        }
        .replace(Regex("\\s+"), " ")
        .take(30)

    val hasTrailingSpace = cleanValue.endsWith(" ")

    val formatted = cleanValue
        .trim()
        .lowercase()
        .split(" ")
        .filter {
            it.isNotBlank()
        }
        .joinToString(" ") { word ->
            word.replaceFirstChar { firstChar ->
                firstChar.titlecase()
            }
        }

    return if (hasTrailingSpace && formatted.length < 30) {
        "$formatted "
    } else {
        formatted
    }
}