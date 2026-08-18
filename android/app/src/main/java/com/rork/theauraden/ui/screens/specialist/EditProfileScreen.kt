package com.rork.theauraden.ui.screens.specialist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.SpecialistProfile
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraDropdownField
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTextField
import com.rork.theauraden.ui.theme.AuraBlue

/** Edit the specialist's public data. */
@Composable
fun EditProfileScreen(
    profile: SpecialistProfile,
    onBack: () -> Unit,
    onSave: (name: String, phone: String, email: String, specialty: String) -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var phone by remember { mutableStateOf(profile.phone) }
    var email by remember { mutableStateOf(profile.email) }
    var specialty by remember { mutableStateOf(profile.specialty) }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Editar perfil",
                eyebrow = "Tu presencia en el estudio",
                onBack = onBack
            )
        },
        bottomAction = {
            AuraPrimaryButton(
                text = "Guardar cambios",
                onClick = { onSave(name, phone, email, specialty) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(22.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AuraAvatar(imageUrl = profile.imageUrl, name = profile.name, size = 104)
                    TextButton(onClick = {}) {
                        Text(
                            text = "Cambiar foto",
                            style = MaterialTheme.typography.labelLarge,
                            color = AuraBlue
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            AuraTextField(
                label = "Nombre completo",
                value = name,
                onValueChange = { name = it }
            )
            Spacer(Modifier.height(18.dp))
            AuraTextField(
                label = "Número de celular",
                value = phone,
                onValueChange = { phone = it },
                keyboardType = KeyboardType.Phone
            )
            Spacer(Modifier.height(18.dp))
            AuraTextField(
                label = "Correo electrónico",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )
            Spacer(Modifier.height(18.dp))
            AuraDropdownField(
                label = "Especialidad",
                value = specialty,
                options = DemoData.specialties,
                onSelect = { specialty = it }
            )
            Spacer(Modifier.height(26.dp))
        }
    }
}
