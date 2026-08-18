package com.rork.theauraden.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraDropdownField
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraLogo
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTextField
import com.rork.theauraden.ui.components.LogoSize
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraInkMuted

/** Specialist sign up. Data is prefilled so the demo reads like a real account. */
@Composable
fun CreateAccountScreen(
    onBack: () -> Unit,
    onRegistered: (String) -> Unit,
    onSignIn: () -> Unit,
    onOpenLegal: () -> Unit
) {
    var name by remember { mutableStateOf("Juanita Cruz") }
    var email by remember { mutableStateOf("juanita@correo.com") }
    var phone by remember { mutableStateOf("+52 55 1234 5678") }
    var specialty by remember { mutableStateOf(DemoData.specialties.first()) }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Crear cuenta",
                eyebrow = "Especialista",
                subtitle = "Al terminar firmarás tu contrato de renta",
                onBack = onBack,
                content = {
                    AuraLogo(size = LogoSize.Compact, modifier = Modifier.padding(bottom = 2.dp))
                }
            )
        },
        bottomAction = {
            AuraPrimaryButton(text = "Registrarme", onClick = { onRegistered(name.trim()) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            AuraTextField(
                label = "Nombre completo",
                value = name,
                onValueChange = { name = it },
                placeholder = "Juanita Cruz"
            )
            Spacer(Modifier.height(18.dp))
            AuraTextField(
                label = "Correo electrónico",
                value = email,
                onValueChange = { email = it },
                placeholder = "juanita@correo.com",
                keyboardType = KeyboardType.Email
            )
            Spacer(Modifier.height(18.dp))
            AuraTextField(
                label = "Número de celular",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "+52 55 1234 5678",
                keyboardType = KeyboardType.Phone
            )
            Spacer(Modifier.height(18.dp))
            AuraDropdownField(
                label = "Especialidad",
                value = specialty,
                options = DemoData.specialties,
                onSelect = { specialty = it }
            )
            Spacer(Modifier.height(22.dp))
            Column(
                modifier = Modifier.padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Al registrarte aceptas nuestros",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = onOpenLegal) {
                    Text(
                        text = "Términos y condiciones y Aviso de privacidad",
                        style = MaterialTheme.typography.labelLarge,
                        color = AuraBlue,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "¿Ya tienes cuenta?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted
                )
                TextButton(onClick = onSignIn) {
                    Text(
                        text = "Inicia sesión",
                        style = MaterialTheme.typography.labelLarge,
                        color = AuraBlue
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
