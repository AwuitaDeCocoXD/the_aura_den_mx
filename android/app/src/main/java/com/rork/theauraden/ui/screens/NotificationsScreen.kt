package com.rork.theauraden.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.AppNotification
import com.rork.theauraden.data.NotificationKind
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraEmptyState
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.StatusAmber
import com.rork.theauraden.ui.theme.StatusGreen

/** Notice centre opened from the bell. Reading the list marks everything as seen. */
@Composable
fun NotificationsScreen(
    notifications: List<AppNotification>,
    onBack: () -> Unit,
    onMarkRead: () -> Unit
) {
    LaunchedEffect(Unit) { onMarkRead() }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Avisos",
                eyebrow = "The Aura Den",
                subtitle = "Lo que pasó mientras no estabas",
                onBack = onBack
            )
        }
    ) { padding ->
        if (notifications.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(28.dp))
                AuraEmptyState(
                    title = "Todo tranquilo",
                    message = "Aquí aparecerán tus confirmaciones, recordatorios de pago y " +
                        "novedades del estudio.",
                    icon = Icons.Rounded.NotificationsNone
                )
            }
            return@AuraDetailScaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 18.dp, bottom = 28.dp)
        ) {
            items(notifications, key = { it.id }) { notice ->
                NotificationRow(
                    notice = notice,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun NotificationRow(
    notice: AppNotification,
    modifier: Modifier = Modifier
) {
    val accent: Color = when (notice.kind) {
        NotificationKind.PAYMENT -> StatusAmber
        NotificationKind.APPOINTMENT -> AuraBlue
        NotificationKind.CONTRACT -> StatusGreen
        NotificationKind.STUDIO -> AuraNavy
    }

    AuraCard(
        modifier = modifier,
        containerColor = if (notice.unread) AuraCream else AuraWhite
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(accent.copy(alpha = 0.13f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (notice.kind) {
                        NotificationKind.PAYMENT -> Icons.Rounded.CreditCard
                        NotificationKind.APPOINTMENT -> Icons.Rounded.EventAvailable
                        NotificationKind.CONTRACT -> Icons.Rounded.Verified
                        NotificationKind.STUDIO -> Icons.Rounded.Storefront
                    },
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(19.dp)
                )
            }
            Spacer(Modifier.width(13.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notice.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AuraNavy,
                        modifier = Modifier.weight(1f)
                    )
                    if (notice.unread) {
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(AuraYellow, CircleShape)
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = notice.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInk
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .background(AuraSandSoft.copy(alpha = 0.55f), RoundedCornerShape(9.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = notice.timeAgo,
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraInkMuted
                    )
                }
            }
        }
    }
}
