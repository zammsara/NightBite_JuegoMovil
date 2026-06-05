package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModel

@Composable
fun PlayerCreationScreen(
    viewModel: PlayerCreationViewModel,
    onPlayerCreated: () -> Unit,
    onBackToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var showExitDialog by remember {
        mutableStateOf(false)
    }

    val nicknameLength = uiState.nickname.trim().length
    val showNicknameError = uiState.nickname.isNotBlank() && nicknameLength < 4
    val isNicknameValid = nicknameLength in 4..8

    val driverNameLength = uiState.driverName.trim().length
    val showDriverNameError = uiState.driverName.isNotBlank() && driverNameLength < 4
    val isDriverNameValid = driverNameLength >= 4

    LaunchedEffect(uiState.isPlayerCreated) {
        if (uiState.isPlayerCreated) {
            onPlayerCreated()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Ficha de Contratación",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Antes de iniciar tu jornada, completa los datos básicos del repartidor asignado.",
            style = MaterialTheme.typography.bodyMedium
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
                    text = "Puesto: Delivery nocturno",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = "Horario: 12:00 a.m. - 3:00 a.m.")
                Text(text = "Restaurante: NightBite Pizza")
                Text(text = "Estado: Pendiente de asignación")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = uiState.nickname,
            onValueChange = { newValue ->
                viewModel.onNicknameChange(newValue.take(8))
            },
            label = {
                Text(text = "Apodo del repartidor")
            },
            singleLine = true,
            isError = showNicknameError,
            supportingText = {
                if (showNicknameError) {
                    Text(
                        text = "El apodo debe tener mínimo 4 caracteres.",
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(
                        text = "Debe tener entre 4 y 8 caracteres.",
                        color = Color.Black
                    )
                }
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.Black
            ),
            colors = playerTextFieldColors(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.driverName,
            onValueChange = { newValue ->
                viewModel.onDriverNameChange(newValue)
            },
            label = {
                Text(text = "Nombre del repartidor")
            },
            singleLine = true,
            isError = showDriverNameError,
            supportingText = {
                if (showDriverNameError) {
                    Text(
                        text = "El nombre debe tener mínimo 4 caracteres.",
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(
                        text = "Debe tener mínimo 4 caracteres.",
                        color = Color.Black
                    )
                }
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.Black
            ),
            colors = playerTextFieldColors(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Género",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        GenderOptions(
            selectedGender = uiState.gender,
            onGenderSelected = viewModel::onGenderSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        PlayerDropdownField(
            label = "Color de casco",
            selectedValue = uiState.helmetColor,
            options = viewModel.helmetColorOptions,
            onOptionSelected = viewModel::onHelmetColorSelected
        )

        Spacer(modifier = Modifier.height(12.dp))

        PlayerDropdownField(
            label = "Tipo de moto",
            selectedValue = uiState.motorcycleType,
            options = viewModel.motorcycleTypeOptions,
            onOptionSelected = viewModel::onMotorcycleTypeSelected
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.onGenderSelected(normalizeGenderForApi(uiState.gender))
                viewModel.createPlayer()
            },
            enabled = !uiState.isLoading && isNicknameValid && isDriverNameValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Crear ficha de repartidor")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                showExitDialog = true
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver")
        }
    }

    if (uiState.errorMessage != null) {
        NightMessageDialog(
            title = "No se pudo continuar",
            message = uiState.errorMessage ?: "",
            confirmText = "Entendido",
            dismissText = null,
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = {
                viewModel.clearError()
            },
            onDismiss = {
                viewModel.clearError()
            }
        )
    }

    if (showExitDialog) {
        NightMessageDialog(
            title = "Salir sin crear ficha",
            message = "Todavía no has creado tu ficha de repartidor. ¿Deseas volver al menú principal sin completar la ficha?",
            confirmText = "Sí, volver",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = {
                showExitDialog = false
                onBackToHome()
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }
}

@Composable
private fun GenderOptions(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    val genderOptions = listOf(
        "FEMENINO" to "Femenino",
        "MASCULINO" to "Masculino"
    )

    genderOptions.forEach { option ->
        val genderValueForApi = option.first
        val genderTextForScreen = option.second

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = normalizeGenderForApi(selectedGender) == genderValueForApi,
                    onClick = {
                        onGenderSelected(genderValueForApi)
                    },
                    role = Role.RadioButton
                )
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = normalizeGenderForApi(selectedGender) == genderValueForApi,
                onClick = {
                    onGenderSelected(genderValueForApi)
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = genderTextForScreen)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(text = label)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.Black
            ),
            colors = playerTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clickable {
                    expanded = true
                }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option)
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun normalizeGenderForApi(gender: String): String {
    return when (gender.trim()) {
        "Femenino" -> "FEMENINO"
        "Masculino" -> "MASCULINO"
        "FEMENINO" -> "FEMENINO"
        "MASCULINO" -> "MASCULINO"
        else -> gender.trim().uppercase()
    }
}

@Composable
private fun playerTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    disabledTextColor = Color.Black,
    errorTextColor = Color.Black,
    focusedLabelColor = Color.Black,
    unfocusedLabelColor = Color.Black,
    disabledLabelColor = Color.Black,
    cursorColor = Color.Black
)