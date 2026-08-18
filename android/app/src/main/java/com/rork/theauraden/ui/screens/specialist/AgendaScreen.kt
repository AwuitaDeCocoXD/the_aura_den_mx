package com.rork.theauraden.ui.screens.specialist

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.Appointment
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.DemoUiState
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraEmptyState
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.DayPill
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraBlueSoft
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/** Weekly agenda with the selected day's appointments. */
@Composable
fun AgendaScreen(
    state: DemoUiState,
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onSelectDay: (String) -> Unit,
    onOpenAppointment: (String) -> Unit,
    onScheduleClient: () -> Unit
) {
    val appointments = state.agendaForSelectedDay
    val nextFreeSlot = DemoData.timeSlots.firstOrNull { slot ->
        appointments.none { it.time == slot }
    }

    AuraTabScaffold(
        role = UserRole.SPECIALIST,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                title = "Mi agenda",
                eyebrow = state.profile.name,
                subtitle = state.selectedDay.fullLabel,
                trailing = {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(AuraWhite.copy(alpha = 0.14f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarMonth,
                            contentDescription = null,
                            tint = AuraYellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onScheduleClient,
                containerColor = AuraBlue,
                contentColor = AuraWhite,
                icon = {
                    Icon(Icons.Rounded.PersonAddAlt1, contentDescription = null)
                },
                text = {
                    Text(
                        text = "Agendar clienta",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                top = 16.dp,
                bottom = 96.dp
            )
        ) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 20.dp
                    )
                ) {
                    items(DemoData.weekDays, key = { it.id }) { day ->
                        val dayHasAppointments = state.appointments.any {
                            it.dayId == day.id && it.status.name == "CONFIRMED"
                        }
                        DayPill(
                            weekday = day.weekdayShort,
                            day = day.dayNumber,
                            selected = day.id == state.selectedDayId,
                            hasDot = dayHasAppointments,
                            onClick = { onSelectDay(day.id) }
                        )
                    }
                }
                Spacer(Modifier.height(22.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = if (appointments.isEmpty()) {
                            "Sin citas"
                        } else {
                            "${appointments.size} citas hoy"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = AuraNavy
                    )
                    if (appointments.isNotEmpty() && nextFreeSlot != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Tu siguiente espacio libre es a las $nextFreeSlot",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraInkMuted
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            if (appointments.isEmpty()) {
                item {
                    AuraEmptyState(
                        title = "No tienes citas hoy",
                        message = "Aprovecha para agendar una clienta o rentar tu espacio " +
                            "en otro horario.",
                        icon = Icons.Rounded.Chair,
                        actionLabel = "Agendar clienta",
                        onAction = onScheduleClient
                    )
                }
            } else {
                items(appointments, key = { it.id }) { appointment ->
                    AgendaAppointmentCard(
                        appointment = appointment,
                        onClick = { onOpenAppointment(appointment.id) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AgendaAppointmentCard(
    appointment: Appointment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(76.dp)
                    .background(AuraBlueSoft, RoundedCornerShape(16.dp))
                    .padding(vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = appointment.time.substringBefore(" "),
                    style = MaterialTheme.typography.titleLarge,
                    color = AuraNavy
                )
                Text(
                    text = appointment.time.substringAfter(" "),
                    style = MaterialTheme.typography.labelSmall,
                    color = AuraBlue
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appointment.clientName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraInk
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = appointment.service,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraBlue
                )
                Spacer(Modifier.height(6.dp))
                Eyebrow(appointment.stationName)
            }
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = AuraInkMuted
            )
        }
    }
}
