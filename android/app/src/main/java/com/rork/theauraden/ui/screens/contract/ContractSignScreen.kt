package com.rork.theauraden.ui.screens.contract

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Draw
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.ContractCopy
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTextField
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.SignaturePad
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraDivider
import com.rork.theauraden.ui.theme.AuraInkFaint
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.StatusRed

/** Step 2: confirm the legal name and draw the signature with a finger. */
@Composable
fun ContractSignScreen(
    defaultName: String,
    onBack: () -> Unit,
    onSign: (String) -> Unit
) {
    var name by remember { mutableStateOf(defaultName) }
    var strokes by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
    val hasSignature = strokes.isNotEmpty()

    AuraDetailScaffold(
        modifier = Modifier.imePadding(),
        header = {
            AuraHeader(
                title = "Firma tu contrato",
                eyebrow = "Paso 2 de 2",
                subtitle = "Confirma tu nombre y firma con el dedo",
                onBack = onBack
            )
        },
        bottomAction = {
            Column {
                AuraPrimaryButton(
                    text = "Firmar contrato",
                    onClick = { onSign(name.trim()) },
                    enabled = hasSignature && name.isNotBlank()
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = AuraInkFaint,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Tu firma queda asociada al folio del contrato",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            AuraTextField(
                label = "Nombre completo",
                value = name,
                onValueChange = { name = it },
                placeholder = "Juanita Cruz"
            )

            Spacer(Modifier.height(20.dp))
            AuraCard(containerColor = AuraWhite) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Draw,
                            contentDescription = null,
                            tint = AuraBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Eyebrow("Tu firma", modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = { strokes = emptyList() },
                            enabled = hasSignature
                        ) {
                            Text(
                                text = "Borrar",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (hasSignature) StatusRed else AuraInkFaint
                            )
                        }
                    }

                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .background(AuraCream, RoundedCornerShape(18.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = 24.dp, vertical = 44.dp)
                                .height(1.dp)
                                .background(AuraDivider)
                        )
                        if (!hasSignature) {
                            Text(
                                text = "Firma aquí con el dedo",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AuraInkFaint,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        SignaturePad(
                            strokes = strokes,
                            onStrokeFinished = { stroke -> strokes = strokes + listOf(stroke) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = if (hasSignature) {
                            "Si no te gustó cómo quedó, toca Borrar y vuelve a intentarlo."
                        } else {
                            "Dibuja tu firma sobre la línea. Puedes borrar y repetirla las veces " +
                                "que quieras."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Al firmar aceptas el ${ContractCopy.TITLE.lowercase()} de The Aura Den " +
                    "en su versión vigente.",
                style = MaterialTheme.typography.bodySmall,
                color = AuraNavy
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}
