package com.rork.theauraden.ui.screens.specialist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.DemoUiState
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraCardMark
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.HoursProgress
import com.rork.theauraden.ui.components.QuickActionTile
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/** Specialist dashboard: next appointment first, then the four quick actions. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpecialistHomeScreen(
    state: DemoUiState,
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onOpenProfile: () -> Unit,
    onOpenRoleSwitcher: () -> Unit,
    onOpenAgenda: () -> Unit,
    onOpenSpaces: () -> Unit,
    onScheduleClient: () -> Unit,
    onOpenPayments: () -> Unit,
    onOpenAppointment: (String) -> Unit
) {
    val profile = state.profile
    val plan = state.activePlan
    val next = state.nextAppointment
    val todayCount = state.appointments.count {
        it.dayId == "tue" && it.status.name == "CONFIRMED"
    }

    AuraTabScaffold(
        role = UserRole.SPECIALIST,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                title = profile.name,
                eyebrow = "Bienvenida de vuelta",
                onBack = null,
                trailing = {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .combinedClickable(
                                onClick = onOpenProfile,
                                onLongClick = onOpenRoleSwitcher
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AuraAvatar(
                            imageUrl = profile.imageUrl,
                            name = profile.name,
                            size = 48
                        )
                    }
                },
                content = {
                    StatusPill(
                        label = "Especialista · ${profile.specialty}",
                        containerColor = AuraWhite.copy(alpha = 0.14f),
                        contentColor = AuraYellow
                    )
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
            Spacer(Modifier.height(18.dp))
            if (next != null) {
                NextAppointmentCard(
                    service = next.service,
                    clientName = next.clientName,
                    time = next.time,
                    station = next.stationName,
                    onClick = { onOpenAppointment(next.id) }
                )
            } else {
                AuraCard(containerColor = AuraWhite) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Eyebrow("Próxima cita")
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Hoy no tienes citas agendadas",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AuraNavy
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Aprovecha para agendar a una clienta o rentar tu espacio.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraInkMuted
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Eyebrow("Accesos rápidos")
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                QuickActionTile(
                    title = "Mi agenda",
                    subtitle = "$todayCount citas hoy",
                    icon = Icons.Rounded.CalendarMonth,
                    onClick = onOpenAgenda,
                    modifier = Modifier.weight(1f)
                )
                QuickActionTile(
                    title = "Rentar espacio",
                    subtitle = "Disponible ahora",
                    icon = Icons.Rounded.Chair,
                    onClick = onOpenSpaces,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                QuickActionTile(
                    title = "Agendar clienta",
                    subtitle = "Dentro de tu horario",
                    icon = Icons.Rounded.PersonAddAlt1,
                    onClick = onScheduleClient,
                    modifier = Modifier.weight(1f)
                )
                QuickActionTile(
                    title = "Mis pagos",
                    subtitle = "Al corriente",
                    icon = Icons.Rounded.Wallet,
                    onClick = onOpenPayments,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))
            MembershipSummaryCard(
                planName = plan.name,
                hoursUsed = profile.hoursUsed,
                totalHours = plan.hours,
                onClick = onOpenPayments
            )
            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun NextAppointmentCard(
    service: String,
    clientName: String,
    time: String,
    station: String,
    onClick: () -> Unit
) {
    AuraCard(containerColor = AuraBlue, onClick = onClick) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Próxima cita",
                    style = MaterialTheme.typography.titleMedium,
                    color = AuraWhite,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(AuraWhite.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = null,
                        tint = AuraYellow,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            StatusPill(label = "HOY", containerColor = AuraYellow, contentColor = AuraNavy)
            Spacer(Modifier.height(12.dp))
            Text(
                text = "$service · $clientName",
                style = MaterialTheme.typography.headlineMedium,
                color = AuraWhite
            )
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    tint = AuraYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    text = "$time · $station",
                    style = MaterialTheme.typography.titleSmall,
                    color = AuraYellow
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = AuraWhite.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun MembershipSummaryCard(
    planName: String,
    hoursUsed: Int,
    totalHours: Int,
    onClick: () -> Unit
) {
    AuraCard(containerColor = AuraCream, onClick = onClick) {
        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Membresía $planName",
                    style = MaterialTheme.typography.titleMedium,
                    color = AuraSand.copy(alpha = 1f),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "${totalHours - hoursUsed} de $totalHours horas disponibles",
                    style = MaterialTheme.typography.titleLarge,
                    color = AuraNavy
                )
                Spacer(Modifier.height(12.dp))
                HoursProgress(used = hoursUsed, total = totalHours)
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = null,
                        tint = AuraSand,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Renueva el 15 de septiembre",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraNavy
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            AuraCardMark(modifier = Modifier.width(56.dp))
        }
    }
}
