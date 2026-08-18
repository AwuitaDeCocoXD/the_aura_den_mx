package com.rork.theauraden.ui.screens.specialist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Schedule
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.Station
import com.rork.theauraden.data.StationStatus
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.DayPill
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.components.TimeChip
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft
import com.rork.theauraden.ui.theme.StatusGrey
import com.rork.theauraden.ui.theme.StatusGreySoft

/** Station detail with date and time selection. */
@Composable
fun ReserveSpaceScreen(
    stationId: String,
    onBack: () -> Unit,
    onConfirm: (stationName: String, dayId: String, time: String) -> Unit
) {
    val station: Station = DemoData.stationById(stationId)
    var selectedDay by remember { mutableStateOf("tue") }
    var selectedTime by remember { mutableStateOf(DemoData.timeSlots.first()) }
    val available = station.status == StationStatus.AVAILABLE

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Reservar espacio",
                eyebrow = station.kind,
                subtitle = station.name,
                onBack = onBack
            )
        },
        bottomAction = {
            AuraPrimaryButton(
                text = if (available) "Confirmar reserva" else "Avisarme cuando se libere",
                onClick = { onConfirm(station.name, selectedDay, selectedTime) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(214.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(AuraSandSoft)
            ) {
                AsyncImage(
                    model = station.imageUrl,
                    contentDescription = station.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                StatusPill(
                    label = if (available) "Disponible ahora" else "Ocupada",
                    containerColor = if (available) StatusGreenSoft else StatusGreySoft,
                    contentColor = if (available) StatusGreen else StatusGrey,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = AuraNavy
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = station.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AuraInkMuted
                )
                Spacer(Modifier.height(16.dp))
                AuraCard(containerColor = AuraCream) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Eyebrow("Incluye")
                        Spacer(Modifier.height(10.dp))
                        station.amenities.forEach { amenity ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = AuraBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(9.dp))
                                Text(
                                    text = amenity,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AuraInk
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Schedule,
                                contentDescription = null,
                                tint = AuraNavy,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(Modifier.width(7.dp))
                            Text(
                                text = station.scheduleLabel,
                                style = MaterialTheme.typography.titleSmall,
                                color = AuraNavy
                            )
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Elige tu fecha",
                    style = MaterialTheme.typography.titleLarge,
                    color = AuraNavy
                )
                Spacer(Modifier.height(12.dp))
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(DemoData.weekDays.take(5), key = { it.id }) { day ->
                    DayPill(
                        weekday = day.weekdayShort,
                        day = day.dayNumber,
                        selected = day.id == selectedDay,
                        onClick = { selectedDay = day.id }
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Horarios disponibles",
                    style = MaterialTheme.typography.titleLarge,
                    color = AuraNavy
                )
                Spacer(Modifier.height(12.dp))
                TimeSlotGrid(
                    slots = DemoData.timeSlots,
                    selected = selectedTime,
                    onSelect = { selectedTime = it }
                )
                Spacer(Modifier.height(18.dp))
                Text(
                    text = "Renta por hora: $${station.hourlyRate} MXN · Se descuenta de tus " +
                        "horas de membresía.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted,
                    fontWeight = FontWeight.Normal
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun TimeSlotGrid(
    slots: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        slots.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { slot ->
                    TimeChip(
                        label = slot,
                        selected = slot == selected,
                        onClick = { onSelect(slot) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}
