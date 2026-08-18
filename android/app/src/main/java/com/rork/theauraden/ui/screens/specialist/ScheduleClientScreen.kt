package com.rork.theauraden.ui.screens.specialist

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.CheckCircle
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
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraDropdownField
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTextField
import com.rork.theauraden.ui.components.DayPill
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.TimeChip
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.StatusGreen

/** Quick booking form for the specialist's own client. */
@Composable
fun ScheduleClientScreen(
    specialistName: String,
    onBack: () -> Unit,
    onConfirm: (
        clientName: String,
        service: String,
        dayId: String,
        time: String,
        station: String,
        notes: String
    ) -> Unit
) {
    var clientName by remember { mutableStateOf("Mariana López") }
    var service by remember { mutableStateOf(DemoData.services.first().name) }
    var selectedDay by remember { mutableStateOf("tue") }
    var selectedTime by remember { mutableStateOf("12:30 pm") }
    var notes by remember { mutableStateOf("Prefiere tono nude rosado") }
    val suggestedStation = if (service == "Pestañas / cejas") {
        "Mesa de pestañas 1"
    } else {
        "Mesa de uñas 2"
    }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Agendar clienta",
                eyebrow = "Nueva cita",
                subtitle = "Para $specialistName",
                onBack = onBack
            )
        },
        bottomAction = {
            AuraPrimaryButton(
                text = "Confirmar cita",
                onClick = {
                    onConfirm(
                        clientName,
                        service,
                        selectedDay,
                        selectedTime,
                        suggestedStation,
                        notes
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
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(22.dp))
                AuraTextField(
                    label = "Nombre de la clienta",
                    value = clientName,
                    onValueChange = { clientName = it },
                    placeholder = "Mariana López"
                )
                Spacer(Modifier.height(18.dp))
                AuraDropdownField(
                    label = "Servicio",
                    value = service,
                    options = DemoData.services.map { it.name },
                    onSelect = { service = it }
                )
                Spacer(Modifier.height(22.dp))
                Eyebrow("Fecha")
                Spacer(Modifier.height(10.dp))
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(DemoData.weekDays, key = { it.id }) { day ->
                    DayPill(
                        weekday = day.weekdayShort,
                        day = day.dayNumber,
                        selected = day.id == selectedDay,
                        onClick = { selectedDay = day.id }
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(22.dp))
                Eyebrow("Hora")
                Spacer(Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DemoData.timeSlots.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            row.forEach { slot ->
                                TimeChip(
                                    label = slot,
                                    selected = slot == selectedTime,
                                    onClick = { selectedTime = slot },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }

                Spacer(Modifier.height(22.dp))
                AuraCard(containerColor = AuraCream) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    AuraSand.copy(alpha = 0.32f),
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Chair,
                                contentDescription = null,
                                tint = AuraNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Eyebrow("Mesa asignada", color = AuraSand)
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = suggestedStation,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AuraNavy
                            )
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text = "Disponible para tu cita",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StatusGreen
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                AuraTextField(
                    label = "Notas opcionales",
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = "Preferencias, alergias, inspiración…",
                    singleLine = false,
                    minLines = 3
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "La clienta recibirá un enlace con los datos de su cita.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
