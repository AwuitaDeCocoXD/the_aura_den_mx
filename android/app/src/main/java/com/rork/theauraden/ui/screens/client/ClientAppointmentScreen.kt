package com.rork.theauraden.ui.screens.client

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.Appointment
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.SpecialistProfile
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraEmptyState
import com.rork.theauraden.ui.components.NotificationBell
import com.rork.theauraden.ui.components.AuraLogo
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.LogoSize
import com.rork.theauraden.ui.components.RatingStars
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraBlueSoft
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.StatusRed

/** Lightweight confirmation the client opens from her link — no full account needed. */
@Composable
fun ClientAppointmentScreen(
    appointment: Appointment?,
    currentRoute: String,
    guestName: String,
    guestInitials: String,
    isNewGuest: Boolean,
    unreadNotifications: Int,
    onTabSelected: (String) -> Unit,
    onOpenNotifications: () -> Unit,
    onCancel: () -> Unit,
    onExplore: () -> Unit,
    onPayService: () -> Unit,
    onOpenRoleSwitcher: () -> Unit
) {
    var showCancelDialog by remember { mutableStateOf(false) }
    val specialist: SpecialistProfile = DemoData.specialists
        .firstOrNull { it.name == appointment?.specialistName }
        ?: DemoData.currentSpecialist

    AuraTabScaffold(
        role = UserRole.CLIENT,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AuraWhite)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NotificationBell(
                        unread = unreadNotifications,
                        onClick = onOpenNotifications,
                        tint = AuraBlue
                    )
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        AuraLogo(
                            size = LogoSize.Compact,
                            textColor = AuraNavy,
                            scriptColor = AuraBlue,
                            arcColor = AuraYellow
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AuraSandSoft)
                            .clickable(onClick = onOpenRoleSwitcher),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = guestInitials,
                            style = MaterialTheme.typography.titleSmall,
                            color = AuraNavy
                        )
                    }
                }
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
            if (appointment == null) {
                Spacer(Modifier.height(18.dp))
                Text(
                    text = if (isNewGuest) {
                        "Bienvenida, ${guestName.split(" ").first()}"
                    } else {
                        "Hola, ${guestName.split(" ").first()}"
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = AuraNavy
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Tu cuenta ya está lista. Solo falta elegir con quién consentirte.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted
                )
                Spacer(Modifier.height(20.dp))
                AuraEmptyState(
                    title = "Aún no tienes citas",
                    message = "Explora a nuestras especialistas y agenda tu primer servicio " +
                        "en The Aura Den.",
                    icon = Icons.Rounded.EventAvailable,
                    actionLabel = "Explorar especialistas",
                    onAction = onExplore
                )
                Spacer(Modifier.height(18.dp))
                StudioLocationCard()
                Spacer(Modifier.height(24.dp))
                return@Column
            }

            Spacer(Modifier.height(14.dp))
            Text(
                text = "Tu cita está confirmada",
                style = MaterialTheme.typography.headlineMedium,
                color = AuraNavy
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Guarda los datos y llega cinco minutos antes.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuraInkMuted
            )

            Spacer(Modifier.height(18.dp))
            AuraCard(containerColor = AuraWhite) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AuraAvatar(
                            imageUrl = specialist.imageUrl,
                            name = specialist.name,
                            size = 76
                        )
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Eyebrow("Con")
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = specialist.name,
                                style = MaterialTheme.typography.headlineSmall,
                                color = AuraNavy
                            )
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RatingStars(rating = specialist.rating, starSize = 14)
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "${specialist.rating}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraInkMuted
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    StatusPill(
                        label = appointment.service,
                        containerColor = AuraBlueSoft,
                        contentColor = AuraBlue
                    )
                    Spacer(Modifier.height(16.dp))
                    DetailLine(
                        icon = Icons.Rounded.CalendarMonth,
                        text = appointment.dateLabel
                    )
                    Spacer(Modifier.height(9.dp))
                    DetailLine(icon = Icons.Rounded.Schedule, text = appointment.time)
                    Spacer(Modifier.height(9.dp))
                    DetailLine(icon = Icons.Rounded.Chair, text = appointment.stationName)
                }
            }

            Spacer(Modifier.height(16.dp))
            StudioLocationCard()

            Spacer(Modifier.height(20.dp))
            AuraPrimaryButton(
                text = "Añadir a mi calendario",
                onClick = {},
                containerColor = AuraYellow,
                contentColor = AuraNavy
            )
            Spacer(Modifier.height(10.dp))
            AuraSecondaryButton(
                text = "Pagar mi servicio",
                onClick = onPayService,
                leadingIcon = Icons.Rounded.CreditCard
            )
            Spacer(Modifier.height(4.dp))
            TextButton(
                onClick = { showCancelDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Cancelar mi cita",
                    style = MaterialTheme.typography.labelLarge,
                    color = StatusRed
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            containerColor = AuraWhite,
            title = {
                Text(
                    text = "¿Seguro que quieres cancelar tu cita?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = AuraNavy
                )
            },
            text = {
                Text(
                    text = "Le avisaremos a tu especialista y liberaremos el horario.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        onCancel()
                    }
                ) {
                    Text(
                        text = "Sí, cancelar",
                        style = MaterialTheme.typography.labelLarge,
                        color = StatusRed
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text(
                        text = "No, mantener",
                        style = MaterialTheme.typography.labelLarge,
                        color = AuraBlue
                    )
                }
            }
        )
    }
}

/** Where the studio is, with a stylized map and the opening hours. */
@Composable
private fun StudioLocationCard() {
    AuraCard(containerColor = AuraCream) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Place,
                    contentDescription = null,
                    tint = AuraSand,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = AuraCopy.ADDRESS_LINE_1,
                        style = MaterialTheme.typography.titleMedium,
                        color = AuraNavy
                    )
                    Text(
                        text = AuraCopy.ADDRESS_LINE_2,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraInkMuted
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            StylizedMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .clip(RoundedCornerShape(18.dp))
            )
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    tint = AuraBlue,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = AuraCopy.OPENING_HOURS,
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
            }
        }
    }
}

@Composable
private fun DetailLine(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AuraBlue,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(9.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = AuraInk
        )
    }
}

/** Stylized, non-interactive location sketch in brand colors. */
@Composable
private fun StylizedMap(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.background(AuraBlueSoft)) {
        val streetColor = AuraWhite
        val blockColor = AuraSandSoft.copy(alpha = 0.55f)
        listOf(0.18f, 0.62f).forEach { fraction ->
            drawRect(
                color = blockColor,
                topLeft = Offset(size.width * fraction, size.height * 0.12f),
                size = Size(size.width * 0.22f, size.height * 0.3f)
            )
        }
        drawRect(
            color = blockColor,
            topLeft = Offset(size.width * 0.35f, size.height * 0.62f),
            size = Size(size.width * 0.4f, size.height * 0.26f)
        )
        drawLine(
            color = streetColor,
            start = Offset(0f, size.height * 0.52f),
            end = Offset(size.width, size.height * 0.46f),
            strokeWidth = 12f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = streetColor,
            start = Offset(size.width * 0.3f, 0f),
            end = Offset(size.width * 0.36f, size.height),
            strokeWidth = 9f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = streetColor,
            start = Offset(size.width * 0.78f, 0f),
            end = Offset(size.width * 0.72f, size.height),
            strokeWidth = 7f,
            cap = StrokeCap.Round
        )
        val marker = Offset(size.width * 0.47f, size.height * 0.45f)
        drawCircle(color = AuraBlue.copy(alpha = 0.18f), radius = 34f, center = marker)
        drawCircle(color = AuraBlue, radius = 15f, center = marker)
        drawCircle(color = AuraYellow, radius = 6f, center = marker)
    }
}
