package com.rork.theauraden.ui.screens.client

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.ClientService
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.SpecialistProfile
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.DayPill
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.RatingStars
import com.rork.theauraden.ui.components.SectionHeading
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/**
 * The guest closes her own booking: service, day and time with the specialist
 * she picked in Explorar. Everything stays in memory.
 */
@Composable
fun BookingScreen(
    specialist: SpecialistProfile,
    onBack: () -> Unit,
    onConfirm: (service: ClientService, dayId: String, time: String) -> Unit
) {
    val services = remember(specialist.id) { servicesFor(specialist) }
    var service by remember(specialist.id) { mutableStateOf(services.first()) }
    var dayId by remember { mutableStateOf(DemoData.weekDays[1].id) }
    var time by remember { mutableStateOf<String?>(null) }

    val selectedDay = DemoData.weekDays.first { it.id == dayId }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Agendar",
                eyebrow = "Con ${specialist.name}",
                subtitle = "Elige servicio, día y hora",
                onBack = onBack
            )
        },
        bottomAction = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (time == null) {
                                "Elige un horario"
                            } else {
                                "${selectedDay.fullLabel} · $time"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraInkMuted
                        )
                        Text(
                            text = "$${"%,d".format(service.price)} MXN",
                            style = MaterialTheme.typography.titleLarge,
                            color = AuraNavy
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                AuraPrimaryButton(
                    text = "Confirmar mi cita",
                    enabled = time != null,
                    onClick = { time?.let { onConfirm(service, dayId, it) } }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(18.dp))
            AuraCard(
                containerColor = AuraWhite,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AuraAvatar(
                        imageUrl = specialist.imageUrl,
                        name = specialist.name,
                        size = 58
                    )
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = specialist.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AuraNavy
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = specialist.specialty,
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraInkMuted
                        )
                        Spacer(Modifier.height(6.dp))
                        RatingStars(rating = specialist.rating, starSize = 13)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionHeading(
                text = "1 · Servicio",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                services.forEach { option ->
                    ServiceOption(
                        service = option,
                        selected = option.id == service.id,
                        onClick = { service = option }
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(14.dp))
            SectionHeading(
                text = "2 · Día",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp)
            ) {
                items(DemoData.weekDays.size) { index ->
                    val day = DemoData.weekDays[index]
                    DayPill(
                        weekday = day.weekdayShort,
                        day = day.dayNumber,
                        selected = day.id == dayId,
                        onClick = {
                            dayId = day.id
                            time = null
                        }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionHeading(
                text = "3 · Hora",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                DemoData.timeSlots.chunked(2).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        row.forEach { slot ->
                            TimeSlotChip(
                                label = slot,
                                selected = slot == time,
                                onClick = { time = slot },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(10.dp))
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
            AuraCard(
                containerColor = AuraCream,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        tint = AuraBlue,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Llega cinco minutos antes. Puedes reprogramar sin costo " +
                            "hasta 24 horas antes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
            }
            Spacer(Modifier.height(28.dp))
        }
    }
}

/** Services the specialist actually offers, always at least one. */
private fun servicesFor(specialist: SpecialistProfile): List<ClientService> {
    val matching = DemoData.services.filter {
        when (specialist.specialty) {
            "Pestañas / cejas" -> it.name.contains("Pestañas")
            "Uñas acrílicas" -> it.name.contains("acrílicas") || it.name.contains("Manicure")
            else -> !it.name.contains("Pestañas")
        }
    }
    return matching.ifEmpty { DemoData.services }
}

@Composable
private fun ServiceOption(
    service: ClientService,
    selected: Boolean,
    onClick: () -> Unit
) {
    val container: Color by animateColorAsState(
        targetValue = if (selected) AuraBlue else AuraWhite,
        animationSpec = tween(200),
        label = "serviceContainer"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = container,
        border = BorderStroke(1.dp, if (selected) AuraBlue else AuraSandSoft)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected) AuraWhite else AuraInk
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = service.durationLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected) AuraWhite.copy(alpha = 0.78f) else AuraInkMuted
                )
            }
            Text(
                text = "$${"%,d".format(service.price)}",
                style = MaterialTheme.typography.titleMedium,
                color = if (selected) AuraYellow else AuraNavy
            )
            if (selected) {
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(AuraYellow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = AuraNavy,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeSlotChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container: Color by animateColorAsState(
        targetValue = if (selected) AuraYellow else AuraWhite,
        animationSpec = tween(200),
        label = "slotContainer"
    )

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = container,
        border = BorderStroke(1.dp, if (selected) AuraYellow else AuraSandSoft)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) AuraNavy else AuraInk
            )
        }
    }
}

/** Small confirmation strip reused by the booking success state. */
@Composable
fun BookingSummaryLine(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Eyebrow("Tu cita")
        Spacer(Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = AuraInk)
    }
}
