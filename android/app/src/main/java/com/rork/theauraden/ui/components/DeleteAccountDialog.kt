package com.rork.theauraden.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.LegalCopy
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkFaint
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.StatusRed
import com.rork.theauraden.ui.theme.StatusRedSoft

/**
 * Two-step account deletion confirmation. Visual only in the demo: nothing is erased,
 * but the flow mirrors what the store guidelines require.
 */
@Composable
fun DeleteAccountDialog(
    accountName: String,
    onDismiss: () -> Unit,
    onConfirmed: () -> Unit
) {
    var secondStep by remember { mutableStateOf(false) }
    var acknowledged by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AuraWhite,
        shape = RoundedCornerShape(26.dp),
        icon = {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(StatusRedSoft, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (secondStep) {
                        Icons.Rounded.DeleteForever
                    } else {
                        Icons.Rounded.WarningAmber
                    },
                    contentDescription = null,
                    tint = StatusRed,
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        title = {
            Text(
                text = if (secondStep) {
                    "Confirma una vez más"
                } else {
                    "¿Eliminar tu cuenta?"
                },
                style = MaterialTheme.typography.headlineSmall,
                color = AuraNavy
            )
        },
        text = {
            Column {
                if (secondStep) {
                    Text(
                        text = "Estás por eliminar la cuenta de $accountName. Esta acción es " +
                            "permanente y no podemos recuperar la información después.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraInkMuted
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (acknowledged) StatusRedSoft else AuraWhite,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { acknowledged = !acknowledged }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    if (acknowledged) StatusRed else AuraWhite,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.5.dp,
                                    color = if (acknowledged) StatusRed else AuraInkFaint,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (acknowledged) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = AuraWhite,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Entiendo que esta acción no se puede deshacer",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraInk
                        )
                    }
                } else {
                    Text(
                        text = "Si eliminas tu cuenta perderás de forma permanente:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraInkMuted
                    )
                    Spacer(Modifier.height(12.dp))
                    LegalCopy.deletionLosses.forEach { item ->
                        Row(
                            modifier = Modifier.padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(StatusRed, CircleShape)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AuraInk
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Si tienes una membresía vigente, te recomendamos hablar con " +
                            "recepción antes de continuar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
            }
        },
        confirmButton = {
            if (secondStep) {
                TextButton(
                    onClick = onConfirmed,
                    enabled = acknowledged
                ) {
                    Text(
                        text = "Eliminar definitivamente",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (acknowledged) StatusRed else AuraInkFaint
                    )
                }
            } else {
                TextButton(onClick = { secondStep = true }) {
                    Text(
                        text = "Continuar",
                        style = MaterialTheme.typography.labelLarge,
                        color = StatusRed
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Mejor no",
                    style = MaterialTheme.typography.labelLarge,
                    color = AuraBlue
                )
            }
        }
    )
}
