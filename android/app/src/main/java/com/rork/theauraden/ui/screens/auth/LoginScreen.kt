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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraLogo
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTextField
import com.rork.theauraden.ui.components.LogoSize
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraInkMuted

/** Simple sign in that drops the specialist straight into her dashboard. */
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onSignedIn: () -> Unit,
    onCreateAccount: () -> Unit
) {
    var identifier by remember { mutableStateOf("juanita@correo.com") }
    var password by remember { mutableStateOf("auraden2025") }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Iniciar sesión",
                eyebrow = "Bienvenida de vuelta",
                subtitle = "Entra para ver tu agenda y tu espacio",
                onBack = onBack,
                content = {
                    AuraLogo(size = LogoSize.Compact, modifier = Modifier.padding(bottom = 2.dp))
                }
            )
        },
        bottomAction = {
            AuraPrimaryButton(text = "Entrar", onClick = onSignedIn)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(26.dp))
            AuraTextField(
                label = "Correo o celular",
                value = identifier,
                onValueChange = { identifier = it },
                placeholder = "juanita@correo.com",
                keyboardType = KeyboardType.Email
            )
            Spacer(Modifier.height(18.dp))
            AuraTextField(
                label = "Contraseña",
                value = password,
                onValueChange = { password = it },
                placeholder = "Tu contraseña",
                isPassword = true,
                keyboardType = KeyboardType.Password
            )
            Spacer(Modifier.height(6.dp))
            TextButton(onClick = {}, modifier = Modifier.align(Alignment.End)) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    style = MaterialTheme.typography.labelMedium,
                    color = AuraBlue
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = "¿Aún no tienes cuenta?",
                style = MaterialTheme.typography.bodyMedium,
                color = AuraInkMuted
            )
            TextButton(onClick = onCreateAccount) {
                Text(
                    text = "Crear cuenta de especialista",
                    style = MaterialTheme.typography.labelLarge,
                    color = AuraBlue
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
