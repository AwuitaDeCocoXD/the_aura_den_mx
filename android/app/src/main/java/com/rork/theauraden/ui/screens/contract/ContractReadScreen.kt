package com.rork.theauraden.ui.screens.contract

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.data.ContractCopy
import com.rork.theauraden.data.SignedContract
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraCardMark
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkFaint
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft
import kotlinx.coroutines.launch

/**
 * Step 1 of the onboarding contract flow: the specialist has to read the whole agreement
 * before the acceptance checkbox unlocks. Also reused read-only from "Mi contrato".
 */
@Composable
fun ContractReadScreen(
    signerName: String,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    readOnly: Boolean = false,
    signedContract: SignedContract? = null
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var reachedEnd by remember { mutableStateOf(readOnly) }
    var accepted by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState.value, scrollState.maxValue) {
        if (scrollState.maxValue > 0 && scrollState.value >= scrollState.maxValue - 24) {
            reachedEnd = true
        }
    }

    val progress = if (scrollState.maxValue == 0) {
        0f
    } else {
        (scrollState.value.toFloat() / scrollState.maxValue.toFloat()).coerceIn(0f, 1f)
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(180),
        label = "readingProgress"
    )

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = if (readOnly) "Mi contrato" else "Contrato de renta",
                eyebrow = if (readOnly) "Documento firmado" else "Paso 1 de 2",
                subtitle = if (readOnly) {
                    "Folio ${signedContract?.folio.orEmpty()}"
                } else {
                    "Léelo completo para poder continuar"
                },
                onBack = onBack,
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .background(AuraWhite.copy(alpha = 0.22f), RoundedCornerShape(50))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (readOnly) 1f else animatedProgress)
                                .height(5.dp)
                                .background(AuraYellow, RoundedCornerShape(50))
                        )
                    }
                }
            )
        },
        bottomAction = {
            if (readOnly) {
                AuraPrimaryButton(text = "Cerrar", onClick = onBack)
            } else {
                Column {
                    AnimatedVisibility(
                        visible = !reachedEnd,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .background(AuraCream, RoundedCornerShape(18.dp))
                                .clickable {
                                    scope.launch { scrollState.animateScrollTo(scrollState.maxValue) }
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Desliza hasta el final para aceptar",
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraInkMuted,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(AuraBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowDownward,
                                    contentDescription = "Ir al final del contrato",
                                    tint = AuraWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = reachedEnd) { accepted = !accepted },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = accepted,
                            onCheckedChange = { accepted = it },
                            enabled = reachedEnd,
                            colors = CheckboxDefaults.colors(
                                checkedColor = AuraBlue,
                                checkmarkColor = AuraWhite,
                                uncheckedColor = AuraInkFaint
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "He leído y acepto los términos de este contrato",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (reachedEnd) AuraInk else AuraInkFaint,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                    AuraPrimaryButton(
                        text = "Continuar",
                        onClick = onContinue,
                        enabled = accepted
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(18.dp))

            if (readOnly && signedContract != null) {
                AuraCard(containerColor = StatusGreenSoft) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Verified,
                            contentDescription = null,
                            tint = StatusGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Firmado por ${signedContract.signerName} el " +
                                "${signedContract.dateLabel} a las ${signedContract.timeLabel}",
                            style = MaterialTheme.typography.bodySmall,
                            color = StatusGreen
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            ContractPaper(signerName = signerName, signedContract = signedContract)

            Spacer(Modifier.height(26.dp))
        }
    }
}

/** The agreement itself, laid out like a printed document. */
@Composable
private fun ContractPaper(
    signerName: String,
    signedContract: SignedContract?
) {
    AuraCard(containerColor = AuraWhite) {
        Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 26.dp)) {
            AuraCardMark(modifier = Modifier.align(Alignment.CenterHorizontally))

            Spacer(Modifier.height(18.dp))
            Text(
                text = ContractCopy.TITLE,
                style = MaterialTheme.typography.headlineSmall,
                color = AuraNavy,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = ContractCopy.SUBTITLE,
                style = MaterialTheme.typography.bodySmall,
                color = AuraInkMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(18.dp))
            HorizontalDivider(color = AuraSandSoft)
            Spacer(Modifier.height(18.dp))

            Text(
                text = ContractCopy.PARTIES,
                style = MaterialTheme.typography.bodyMedium,
                color = AuraInk
            )

            ContractCopy.sections.forEach { section ->
                Spacer(Modifier.height(22.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(AuraSandSoft, RoundedCornerShape(9.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = section.number,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AuraNavy
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AuraNavy,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(9.dp))
                Text(
                    text = section.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInk
                )
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = AuraSandSoft)
            Spacer(Modifier.height(18.dp))

            Text(
                text = ContractCopy.CLOSING,
                style = MaterialTheme.typography.bodyMedium,
                color = AuraInk
            )

            Spacer(Modifier.height(26.dp))
            Eyebrow("Por la especialista")
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = AuraInkFaint)
            Spacer(Modifier.height(8.dp))
            Text(
                text = signedContract?.signerName ?: signerName,
                style = MaterialTheme.typography.titleMedium,
                color = AuraNavy
            )
            Text(
                text = signedContract?.let {
                    "Folio ${it.folio} · ${it.dateLabel}, ${it.timeLabel}"
                } ?: "Pendiente de firma · ${AuraCopy.ADDRESS_LINE_2}",
                style = MaterialTheme.typography.bodySmall,
                color = AuraInkMuted
            )
        }
    }
}
