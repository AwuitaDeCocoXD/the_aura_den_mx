package com.rork.theauraden.ui.screens.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.Spa
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
import com.rork.theauraden.data.Appointment
import com.rork.theauraden.data.AppointmentStatus
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AppointmentStatusPill
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraEmptyState
import com.rork.theauraden.ui.components.AuraFilterChip
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraSandSoft

private const val UPCOMING = "Próximas"
private const val PAST = "Pasadas"
private val historyFilters = listOf(UPCOMING, PAST)

/** The client's own appointment history: upcoming and past visits. */
@Composable
fun ClientHistoryScreen(
    upcoming: List<Appointment>,
    past: List<Appointment>,
    currentRoute: String,
    guestName: String,
    reviewedIds: Set<String>,
    onTabSelected: (String) -> Unit,
    onBack: () -> Unit,
    onReview: (String) -> Unit,
    onExplore: () -> Unit
) {
    var filter by remember { mutableStateOf(UPCOMING) }
    val visible = if (filter == UPCOMING) upcoming else past

    AuraTabScaffold(
        role = UserRole.CLIENT,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                onBack = onBack,
                title = "Mis citas",
                eyebrow = guestName,
                subtitle = "Tu historial en The Aura Den"
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 16.dp, bottom = 26.dp)
        ) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(historyFilters) { option ->
                        AuraFilterChip(
                            label = option,
                            selected = option == filter,
                            onClick = { filter = option }
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
            }

            if (visible.isEmpty()) {
                item {
                    AuraEmptyState(
                        title = "Aún no tienes citas",
                        message = if (filter == UPCOMING) {
                            "Cuando agendes tu próximo servicio aparecerá aquí con todos los " +
                                "datos."
                        } else {
                            "Tus visitas anteriores aparecerán en esta lista."
                        },
                        icon = Icons.Rounded.EventAvailable,
                        actionLabel = "Explorar especialistas",
                        onAction = onExplore
                    )
                }
            } else {
                items(visible, key = { it.id }) { appointment ->
                    val canReview = appointment.status == AppointmentStatus.COMPLETED &&
                        appointment.id !in reviewedIds
                    if (canReview) {
                        ReviewPromptCard(
                            appointment = appointment,
                            onReview = { onReview(appointment.id) },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                    } else {
                        ClientAppointmentRow(
                            appointment = appointment,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClientAppointmentRow(
    appointment: Appointment,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(AuraSandSoft, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Spa,
                    contentDescription = null,
                    tint = AuraBlue,
                    modifier = Modifier.size(21.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appointment.service,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraInk
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "Con ${appointment.specialistName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted
                )
                Spacer(Modifier.height(6.dp))
                Eyebrow("${appointment.dateLabel} · ${appointment.time}")
            }
            AppointmentStatusPill(
                status = if (appointment.status == AppointmentStatus.CONFIRMED) {
                    AppointmentStatus.CONFIRMED
                } else {
                    appointment.status
                }
            )
        }
    }
}
