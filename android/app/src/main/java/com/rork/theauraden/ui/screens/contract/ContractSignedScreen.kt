package com.rork.theauraden.ui.screens.contract

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.SignedContract
import com.rork.theauraden.ui.components.AuraArc
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraNavyDeep
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/** Step 3: confirmation with the assigned folio and the timestamp of the signature. */
@Composable
fun ContractSignedScreen(
    contract: SignedContract,
    onContinue: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.6f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "contractSignedScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))
            Box(
                modifier = Modifier
                    .size(116.dp)
                    .scale(scale)
                    .background(AuraYellow, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = AuraNavyDeep,
                    modifier = Modifier.size(58.dp)
                )
            }

            Spacer(Modifier.height(26.dp))
            AuraArc(
                modifier = Modifier.size(width = 64.dp, height = 24.dp),
                color = AuraYellow.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = "¡Contrato firmado!",
                style = MaterialTheme.typography.displayMedium,
                color = AuraWhite,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))
            Text(
                text = "Ya puedes reservar tu espacio en The Aura Den. Guardamos una copia en " +
                    "tu perfil.",
                style = MaterialTheme.typography.bodyLarge,
                color = AuraWhite.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(26.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AuraNavy.copy(alpha = 0.45f), RoundedCornerShape(24.dp))
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                ContractFactRow(
                    icon = Icons.Rounded.Tag,
                    label = "Folio del contrato",
                    value = contract.folio
                )
                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = AuraWhite.copy(alpha = 0.18f))
                Spacer(Modifier.height(14.dp))
                ContractFactRow(
                    icon = Icons.Rounded.Schedule,
                    label = "Fecha y hora de firma",
                    value = "${contract.dateLabel} · ${contract.timeLabel}"
                )
            }

            Spacer(Modifier.height(14.dp))
            Text(
                text = "Firmado electrónicamente por ${contract.signerName}",
                style = MaterialTheme.typography.bodySmall,
                color = AuraWhite.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))
            Column(modifier = Modifier.navigationBarsPadding()) {
                AuraPrimaryButton(
                    text = "Continuar a mi cuenta",
                    onClick = onContinue,
                    containerColor = AuraWhite,
                    contentColor = AuraBlue
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ContractFactRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(AuraWhite.copy(alpha = 0.12f), RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AuraYellow,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = AuraWhite.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = AuraYellow
            )
        }
    }
}
