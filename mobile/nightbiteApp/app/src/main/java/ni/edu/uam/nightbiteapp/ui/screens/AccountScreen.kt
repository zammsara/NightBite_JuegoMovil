package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.AccountCredentialsViewModel

@Composable
fun AccountScreen(
    userSession: UserSession,
    viewModel: AccountCredentialsViewModel,
    onBackToSettings: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var showExitConfirmation by remember {
        mutableStateOf(false)
    }

    var usernameError by remember {
        mutableStateOf<String?>(null)
    }

    var currentPasswordError by remember {
        mutableStateOf<String?>(null)
    }

    var newPasswordError by remember {
        mutableStateOf<String?>(null)
    }

    var confirmPasswordError by remember {
        mutableStateOf<String?>(null)
    }

    val hasUnsavedChanges =
        uiState.newUsername.isNotBlank() ||
                uiState.currentPassword.isNotBlank() ||
                uiState.newPassword.isNotBlank() ||
                uiState.confirmNewPassword.isNotBlank()

    fun clearErrors() {
        usernameError = null
        currentPasswordError = null
        newPasswordError = null
        confirmPasswordError = null
    }

    fun sanitizeUsername(value: String): String {
        return value
            .lowercase()
            .filter { character ->
                character in 'a'..'z' ||
                        character in '0'..'9' ||
                        character == '_'
            }
            .take(16)
    }

    fun validateFields(): Boolean {
        clearErrors()

        var isValid = true

        val username = uiState.newUsername.trim()
        val currentPassword = uiState.currentPassword
        val newPassword = uiState.newPassword
        val confirmPassword = uiState.confirmNewPassword

        if (username.isBlank()) {
            usernameError = "No se llenó el campo nombre de usuario."
            isValid = false
        } else if (username == userSession.username) {
            usernameError = "El nuevo nombre de usuario debe ser diferente al usuario actual."
            isValid = false
        }

        if (currentPassword.isBlank()) {
            currentPasswordError = "No se llenó el campo contraseña actual."
            isValid = false
        }

        if (newPassword.isBlank()) {
            newPasswordError = "No se llenó el campo nueva contraseña."
            isValid = false
        } else if (newPassword.length < 8) {
            newPasswordError = "La nueva contraseña debe tener mínimo 8 caracteres."
            isValid = false
        } else if (newPassword == currentPassword) {
            newPasswordError = "La nueva contraseña debe ser diferente de la contraseña actual."
            isValid = false
        }

        if (confirmPassword.isBlank()) {
            confirmPasswordError = "No se llenó el campo confirmar nueva contraseña."
            isValid = false
        } else if (newPassword != confirmPassword) {
            confirmPasswordError = "La nueva contraseña y la confirmación deben coincidir."
            isValid = false
        }

        return isValid
    }

    fun requestExit() {
        if (uiState.isLoading) {
            return
        }

        if (hasUnsavedChanges) {
            showExitConfirmation = true
        } else {;
            onBackToSettings()
        }
    }

    BackHandler {
        requestExit()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Actualizar credenciales",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = "Nombre de usuario",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Solo letras minúsculas, números y guion bajo. Máximo 16 caracteres.",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.newUsername,
                    onValueChange = { value ->
                        val cleanUsername = sanitizeUsername(value)

                        usernameError = null
                        viewModel.onNewUsernameChange(cleanUsername)
                    },
                    label = {
                        Text(text = "Nuevo nombre de usuario")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    isError = usernameError != null,
                    supportingText = {
                        usernameError?.let { error ->
                            Text(text = error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Debes escribir tu contraseña actual, una nueva contraseña y confirmarla.",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.currentPassword,
                    onValueChange = { value ->
                        currentPasswordError = null
                        viewModel.onCurrentPasswordChange(value)
                    },
                    label = {
                        Text(text = "Contraseña actual")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    isError = currentPasswordError != null,
                    supportingText = {
                        currentPasswordError?.let { error ->
                            Text(text = error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.newPassword,
                    onValueChange = { value ->
                        newPasswordError = null
                        viewModel.onNewPasswordChange(value)
                    },
                    label = {
                        Text(text = "Nueva contraseña")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    isError = newPasswordError != null,
                    supportingText = {
                        newPasswordError?.let { error ->
                            Text(text = error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.confirmNewPassword,
                    onValueChange = { value ->
                        confirmPasswordError = null
                        viewModel.onConfirmNewPasswordChange(value)
                    },
                    label = {
                        Text(text = "Confirmar nueva contraseña")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    isError = confirmPasswordError != null,
                    supportingText = {
                        confirmPasswordError?.let { error ->
                            Text(text = error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                uiState.errorMessage?.let { errorMessage ->
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (validateFields()) {
                            viewModel.onApplyChangesClick(
                                currentUsername = userSession.username
                            )
                        }
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = "Aplicar cambios")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = {
                requestExit()
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cancelar")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (uiState.showConfirmDialog) {
        NightMessageDialog(
            title = "Confirmar cambios",
            message = "¿Estás segura de aplicar los cambios en tus credenciales?",
            confirmText = "Sí, aplicar",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = {
                viewModel.applyConfirmedChanges(
                    userId = userSession.userId,
                    currentUsername = userSession.username
                )
            },
            onDismiss = {
                viewModel.dismissConfirmDialog()
            }
        )
    }

    if (uiState.showSessionExpiredDialog) {
        NightMessageDialog(
            title = "Credenciales actualizadas",
            message = "Los cambios fueron aplicados correctamente. Debes iniciar sesión nuevamente.",
            confirmText = "Aceptar",
            dismissText = null,
            icon = Icons.Default.CheckCircle,
            iconColor = CheeseYellow,
            onConfirm = {
                viewModel.clearSessionAndFinish {
                    onNavigateToLogin()
                }
            },
            onDismiss = null
        )
    }

    if (showExitConfirmation) {
        NightMessageDialog(
            title = "Salir sin guardar",
            message = "Tienes cambios sin aplicar. ¿Deseas salir y descartarlos?",
            confirmText = "Salir",
            dismissText = "Continuar editando",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = {
                showExitConfirmation = false
                onBackToSettings()
            },
            onDismiss = {
                showExitConfirmation = false
            }
        )
    }
}