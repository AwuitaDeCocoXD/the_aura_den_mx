package com.rork.theauraden.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rork.theauraden.ui.components.AuraArc
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraNavyDeep
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/** Shared confirmation moment after a reservation or a new appointment. */
@Composable
fun SuccessScreen(
    title: String,
    message: String,
    detail: String?,
    primaryLabel: String,
    onPrimary: () -> Unit,
    secondaryLabel: String? = null,
    onSecondary: (() -> Unit)? = null
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.6f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "successScale"
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
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
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
                modifier = Modifier
                    .size(width = 64.dp, height = 24.dp),
                color = AuraYellow.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium,
                color = AuraWhite,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = AuraWhite.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )
            if (detail != null) {
                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AuraNavy.copy(alpha = 0.45f), CircleShape)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.titleSmall,
                        color = AuraYellow,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Column(modifier = Modifier.navigationBarsPadding()) {
                AuraPrimaryButton(
                    text = primaryLabel,
                    onClick = onPrimary,
                    containerColor = AuraWhite,
                    contentColor = AuraBlue
                )
                if (secondaryLabel != null && onSecondary != null) {
                    Spacer(Modifier.height(10.dp))
                    AuraSecondaryButton(text = secondaryLabel, onClick = onSecondary)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
