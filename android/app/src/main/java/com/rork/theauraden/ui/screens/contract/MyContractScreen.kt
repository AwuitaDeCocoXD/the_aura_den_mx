package com.rork.theauraden.ui.screens.contract

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Draw
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.data.ContractCopy
import com.rork.theauraden.data.SignedContract
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.InfoRow
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.StatusAmber
import com.rork.theauraden.ui.theme.StatusAmberSoft
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft

/** Summary of the signed agreement, reachable from the specialist profile. */
@Composable
fun MyContractScreen(
    contract: SignedContract?,
    planName: String,
    onBack: () -> Unit,
    onReadContract: () -> Unit,
    onSignContract: () -> Unit
) {
    var pdfRequested by remember { mutableStateOf(false) }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Mi contrato",
                eyebrow = "Renta de espacio",
                subtitle = if (contract != null) {
                    "Vigente · Membresía $planName"
                } else {
                    "Aún no has firmado tu contrato"
                },
                onBack = onBack,
                content = {
                    StatusPill(
                        label = if (contract != null) "Contrato firmado" else "Pendiente de firma",
                        containerColor = if (contract != null) AuraYellow else AuraWhite,
                        contentColor = AuraNavy,
                        icon = if (contract != null) Icons.Rounded.Verified else Icons.Rounded.Draw
                    )
                }
            )
        },
        bottomAction = {
            if (contract != null) {
                AuraSecondaryButton(
                    text = "Ver contrato completo",
                    onClick = onReadContract,
                    leadingIcon = Icons.Rounded.Description
                )
            } else {
                AuraPrimaryButton(
                    text = "Firmar contrato",
                    onClick = onSignContract,
                    leadingIcon = Icons.Rounded.Draw
                )
            }
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

            if (contract == null) {
                AuraCard(containerColor = StatusAmberSoft) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = StatusAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Contrato pendiente",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = StatusAmber
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Para poder reservar una estación necesitas leer y firmar el " +
                                "contrato de renta de espacio. Te toma menos de dos minutos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraInk
                        )
                    }
                }
                Spacer(Modifier.height(26.dp))
                return@Column
            }

            ContractHeroCard(contract = contract)

            Spacer(Modifier.height(16.dp))
            AuraCard {
                Column {
                    InfoRow(
                        label = "Folio del contrato",
                        value = contract.folio,
                        icon = Icons.Rounded.Tag
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
                    InfoRow(
                        label = "Fecha de firma",
                        value = "${contract.dateLabel} · ${contract.timeLabel}",
                        icon = Icons.Rounded.CalendarMonth
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
                    InfoRow(
                        label = "Firmado por",
                        value = contract.signerName,
                        icon = Icons.Rounded.Draw
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
                    InfoRow(
                        label = "Documento",
                        value = ContractCopy.TITLE,
                        icon = Icons.Rounded.Description,
                        showChevron = true,
                        onClick = onReadContract
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            AuraCard(containerColor = AuraCream, onClick = { pdfRequested = true }) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(AuraSand.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Download,
                            contentDescription = null,
                            tint = AuraNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Descargar PDF",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AuraInk
                        )
                        Text(
                            text = "Copia del contrato con tu firma",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraInkMuted
                        )
                    }
                }
            }

            AnimatedVisibility(visible = pdfRequested, enter = fadeIn(), exit = fadeOut()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(StatusGreenSoft, RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = StatusGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "La descarga en PDF estará disponible en la versión final de la app.",
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusGreen
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                text = "Este contrato es una simulación visual sin validez legal. " +
                    "${AuraCopy.BRAND_NAME} · ${AuraCopy.ADDRESS_LINE_1}, " +
                    AuraCopy.ADDRESS_LINE_2,
                style = MaterialTheme.typography.bodySmall,
                color = AuraInkMuted
            )
            Spacer(Modifier.height(26.dp))
        }
    }
}

@Composable
private fun ContractHeroCard(contract: SignedContract) {
    AuraCard(containerColor = AuraWhite) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(StatusGreenSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Verified,
                        contentDescription = null,
                        tint = StatusGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Eyebrow("Estado")
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = "Contrato firmado",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AuraNavy
                    )
                }
            }

            Spacer(Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AuraSandSoft.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Column {
                    Eyebrow("Firmado por", color = AuraSand)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = contract.signerName,
                        style = MaterialTheme.typography.displaySmall,
                        color = AuraNavy
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "${contract.dateLabel} · ${contract.timeLabel}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraInkMuted
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            Text(
                text = "Folio ${contract.folio}",
                style = MaterialTheme.typography.labelLarge,
                color = AuraBlue
            )
        }
    }
}
