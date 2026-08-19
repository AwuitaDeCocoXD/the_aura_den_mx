package com.rork.theauraden.ui.screens.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCut
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/** The two kinds of account the studio hands out. */
enum class SignUpMode { SPECIALIST, GUEST }

/**
 * Sign up for both audiences. The specialist path continues into the rental
 * agreement; the guest path only collects contact data and lets her book.
 */
@Composable
fun CreateAccountScreen(
    initialMode: SignUpMode,
    onBack: () -> Unit,
    onRegistered: (String) -> Unit,
    onRegisteredGuest: (String) -> Unit,
    onSignIn: () -> Unit,
    onOpenLegal: () -> Unit
) {
    var mode by remember { mutableStateOf(initialMode) }
    var specialistName by remember { mutableStateOf("Juanita Cruz") }
    var specialistEmail by remember { mutableStateOf("juanita@correo.com") }
    var specialistPhone by remember { mutableStateOf("+52 55 1234 5678") }
    var specialty by remember { mutableStateOf(DemoData.specialties.first()) }

    var guestName by remember { mutableStateOf("") }
    var guestEmail by remember { mutableStateOf("") }
    var guestPhone by remember { mutableStateOf("") }

    val isGuest = mode == SignUpMode.GUEST
    val canSubmit = if (isGuest) guestName.isNotBlank() else specialistName.isNotBlank()

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = if (isGuest) "Crear cuenta de invitada" else "Crear cuenta",
                eyebrow = if (isGuest) "Invitada" else "Especialista",
                subtitle = if (isGuest) {
                    "En un minuto ya puedes agendar tu primera cita"
                } else {
                    "Al terminar firmarás tu contrato de renta"
                },
                onBack = onBack,
                content = {
                    AuraLogo(size = LogoSize.Compact, modifier = Modifier.padding(bottom = 2.dp))
                }
            )
        },
        bottomAction = {
            AuraPrimaryButton(
                text = if (isGuest) "Crear mi cuenta" else "Registrarme",
                enabled = canSubmit,
                onClick = {
                    if (isGuest) {
                        onRegisteredGuest(guestName.trim())
                    } else {
                        onRegistered(specialistName.trim())
                    }
                }
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
            Spacer(Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ModeCard(
                    label = "Soy especialista",
                    detail = "Rento un espacio",
                    icon = Icons.Rounded.ContentCut,
                    selected = !isGuest,
                    onClick = { mode = SignUpMode.SPECIALIST },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                ModeCard(
                    label = "Soy invitada",
                    detail = "Vengo por un servicio",
                    icon = Icons.Rounded.EventAvailable,
                    selected = isGuest,
                    onClick = { mode = SignUpMode.GUEST },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))
            if (isGuest) {
                AuraTextField(
                    label = "Nombre completo",
                    value = guestName,
                    onValueChange = { guestName = it },
                    placeholder = "Ana Sofía Herrera"
                )
                Spacer(Modifier.height(18.dp))
                AuraTextField(
                    label = "Correo electrónico",
                    value = guestEmail,
                    onValueChange = { guestEmail = it },
                    placeholder = "ana@correo.com",
                    keyboardType = KeyboardType.Email
                )
                Spacer(Modifier.height(18.dp))
                AuraTextField(
                    label = "Número de celular",
                    value = guestPhone,
                    onValueChange = { guestPhone = it },
                    placeholder = "+52 55 0000 0000",
                    keyboardType = KeyboardType.Phone
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Las invitadas no firman contrato: con estos datos ya puedes " +
                        "agendar con cualquiera de nuestras especialistas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
            } else {
                AuraTextField(
                    label = "Nombre completo",
                    value = specialistName,
                    onValueChange = { specialistName = it },
                    placeholder = "Juanita Cruz"
                )
                Spacer(Modifier.height(18.dp))
                AuraTextField(
                    label = "Correo electrónico",
                    value = specialistEmail,
                    onValueChange = { specialistEmail = it },
                    placeholder = "juanita@correo.com",
                    keyboardType = KeyboardType.Email
                )
                Spacer(Modifier.height(18.dp))
                AuraTextField(
                    label = "Número de celular",
                    value = specialistPhone,
                    onValueChange = { specialistPhone = it },
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
            }

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

/** One of the two account paths, shown as a large tappable tile. */
@Composable
private fun ModeCard(
    label: String,
    detail: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container: Color by animateColorAsState(
        targetValue = if (selected) AuraBlue else AuraWhite,
        animationSpec = tween(220),
        label = "modeContainer"
    )
    val content: Color by animateColorAsState(
        targetValue = if (selected) AuraWhite else AuraNavy,
        animationSpec = tween(220),
        label = "modeContent"
    )

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = container,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (selected) AuraBlue else AuraInkMuted.copy(alpha = 0.25f)
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        if (selected) AuraYellow else AuraBlue.copy(alpha = 0.09f),
                        RoundedCornerShape(13.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (selected) AuraNavy else AuraBlue,
                    modifier = Modifier.size(19.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = content
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = detail,
                style = MaterialTheme.typography.bodySmall,
                color = if (selected) AuraWhite.copy(alpha = 0.8f) else AuraInk.copy(alpha = 0.6f)
            )
        }
    }
}
