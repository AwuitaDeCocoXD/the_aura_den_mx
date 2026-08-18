package com.rork.theauraden.ui.screens.checkout

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.data.PaymentKind
import com.rork.theauraden.data.PaymentRecord
import com.rork.theauraden.data.PaymentStatus
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraCardMark
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.PaymentStatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.StatusAmber
import com.rork.theauraden.ui.theme.StatusAmberSoft
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft
import com.rork.theauraden.ui.theme.StatusRed
import com.rork.theauraden.ui.theme.StatusRedSoft

/** Receipt for both money flows, covering paid, pending and failed states. */
@Composable
fun ReceiptScreen(
    payment: PaymentRecord,
    onBack: () -> Unit,
    onPrimary: () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    val isMembership = payment.kind == PaymentKind.MEMBERSHIP
    val (container, content) = when (payment.status) {
        PaymentStatus.PAID -> StatusGreenSoft to StatusGreen
        PaymentStatus.PENDING -> StatusAmberSoft to StatusAmber
        PaymentStatus.FAILED -> StatusRedSoft to StatusRed
    }
    val headline = when (payment.status) {
        PaymentStatus.PAID -> "Pago confirmado"
        PaymentStatus.PENDING -> "Pago en proceso"
        PaymentStatus.FAILED -> "El pago no se completó"
    }
    val message = when (payment.status) {
        PaymentStatus.PAID -> if (isMembership) {
            "Tu membresía queda activa y tus horas ya están disponibles."
        } else {
            "Tu servicio quedó cubierto. Te esperamos en el estudio."
        }
        PaymentStatus.PENDING -> "Tu transferencia SPEI se confirma en un día hábil. " +
            "Te avisamos en cuanto se acredite."
        PaymentStatus.FAILED -> "El banco rechazó el cargo. Intenta con otro método de pago."
    }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Comprobante",
                eyebrow = if (isMembership) "Renta del espacio" else "Servicio",
                subtitle = "Folio ${payment.folio}",
                onBack = onBack
            )
        },
        bottomAction = {
            Column {
                AuraPrimaryButton(
                    text = when (payment.status) {
                        PaymentStatus.FAILED -> "Intentar de nuevo"
                        else -> "Listo"
                    },
                    onClick = if (payment.status == PaymentStatus.FAILED && onRetry != null) {
                        onRetry
                    } else {
                        onPrimary
                    }
                )
                if (payment.status != PaymentStatus.FAILED) {
                    Spacer(Modifier.height(8.dp))
                    AuraSecondaryButton(text = "Descargar comprobante", onClick = {})
                }
            }
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
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .background(container, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (payment.status) {
                        PaymentStatus.PAID -> Icons.Rounded.Check
                        PaymentStatus.PENDING -> Icons.Rounded.HourglassTop
                        PaymentStatus.FAILED -> Icons.Rounded.Close
                    },
                    contentDescription = null,
                    tint = content,
                    modifier = Modifier.size(44.dp)
                )
            }
            Spacer(Modifier.height(18.dp))
            Text(
                text = headline,
                style = MaterialTheme.typography.headlineLarge,
                color = AuraNavy,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = AuraInkMuted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            PaymentStatusPill(payment.status)

            Spacer(Modifier.height(24.dp))
            AuraCard(containerColor = AuraCream) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Column(modifier = Modifier.weight(1f)) {
                            Eyebrow("Concepto", color = AuraSand)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = payment.concept,
                                style = MaterialTheme.typography.titleLarge,
                                color = AuraNavy
                            )
                        }
                        AuraCardMark(modifier = Modifier.width(52.dp))
                    }
                    Spacer(Modifier.height(18.dp))
                    ReceiptRow("Folio", payment.folio)
                    Spacer(Modifier.height(10.dp))
                    ReceiptRow("Fecha", payment.dateLabel)
                    Spacer(Modifier.height(10.dp))
                    ReceiptRow("Método", payment.method)
                    if (payment.payerName != null) {
                        Spacer(Modifier.height(10.dp))
                        ReceiptRow("Pagado por", payment.payerName)
                    }
                    Spacer(Modifier.height(10.dp))
                    ReceiptRow(
                        "Factura CFDI",
                        if (payment.invoiceRequested) "Solicitada" else "No solicitada"
                    )
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleLarge,
                            color = AuraNavy,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$${"%,d".format(payment.amount)} MXN",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AuraBlue
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            AuraCard(containerColor = AuraWhite) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ReceiptLong,
                        contentDescription = null,
                        tint = AuraSand,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (isMembership) {
                                "Cobro de renta · The Aura Den"
                            } else {
                                "Cobro de servicio · procesado por la app"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = AuraInk
                        )
                        Text(
                            text = "${AuraCopy.ADDRESS_LINE_1}, ${AuraCopy.ADDRESS_LINE_2}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraInkMuted
                        )
                    }
                }
            }
            Spacer(Modifier.height(26.dp))
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AuraInkMuted,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = AuraInk
        )
    }
}
