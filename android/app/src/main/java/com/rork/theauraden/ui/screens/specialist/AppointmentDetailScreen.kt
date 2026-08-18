package com.rork.theauraden.ui.screens.specialist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.Appointment
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.ui.components.AppointmentStatusPill
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDangerTextButton
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.InfoRow
import com.rork.theauraden.ui.components.TimeChip
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.StatusRed

/** Appointment detail with reschedule and cancel actions. */
@Composable
fun AppointmentDetailScreen(
    appointment: Appointment,
    onBack: () -> Unit,
    onReschedule: (time: String) -> Unit,
    onCancel: () -> Unit
) {
    var showCancelDialog by remember { mutableStateOf(false) }
    var showReschedule by remember { mutableStateOf(false) }
    var newTime by remember { mutableStateOf(appointment.time) }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Detalle de cita",
                eyebrow = appointment.dateLabel,
                onBack = onBack
            )
        },
        bottomAction = {
            Column {
                AuraSecondaryButton(
                    text = if (showReschedule) "Guardar nuevo horario" else "Reprogramar",
                    onClick = {
                        if (showReschedule) {
                            onReschedule(newTime)
                        } else {
                            showReschedule = true
                        }
                    }
                )
                Spacer(Modifier.height(4.dp))
                AuraDangerTextButton(
                    text = "Cancelar cita",
                    onClick = { showCancelDialog = true }
                )
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
            Spacer(Modifier.height(22.dp))
            Text(
                text = appointment.clientName,
                style = MaterialTheme.typography.displaySmall,
                color = AuraNavy
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = appointment.service,
                style = MaterialTheme.typography.titleMedium,
                color = AuraBlue
            )
            Spacer(Modifier.height(14.dp))
            AppointmentStatusPill(appointment.status)

            Spacer(Modifier.height(22.dp))
            AuraCard(containerColor = AuraWhite) {
                Column {
                    InfoRow(
                        label = "Fecha",
                        value = appointment.dateLabel,
                        icon = Icons.Rounded.CalendarMonth
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
                    InfoRow(
                        label = "Hora",
                        value = appointment.time,
                        icon = Icons.Rounded.Schedule
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
                    InfoRow(
                        label = "Mesa",
                        value = appointment.stationName,
                        icon = Icons.Rounded.Chair
                    )
                }
            }

            if (appointment.notes != null) {
                Spacer(Modifier.height(16.dp))
                AuraCard(containerColor = AuraCream) {
                    Row(modifier = Modifier.padding(18.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Notes,
                            contentDescription = null,
                            tint = AuraSand,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Eyebrow("Notas", color = AuraSand)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = appointment.notes,
                                style = MaterialTheme.typography.bodyLarge,
                                color = AuraInk
                            )
                        }
                    }
                }
            }

            if (showReschedule) {
                Spacer(Modifier.height(22.dp))
                Text(
                    text = "Nuevo horario",
                    style = MaterialTheme.typography.titleLarge,
                    color = AuraNavy
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Elige otra franja disponible para ${appointment.clientName}.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted
                )
                Spacer(Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DemoData.timeSlots.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            row.forEach { slot ->
                                TimeChip(
                                    label = slot,
                                    selected = slot == newTime,
                                    onClick = { newTime = slot },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            containerColor = AuraWhite,
            title = {
                Text(
                    text = "¿Seguro que quieres cancelar esta cita?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = AuraNavy
                )
            },
            text = {
                Text(
                    text = "${appointment.clientName} recibirá un aviso de que su cita de " +
                        "${appointment.service} fue cancelada.",
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
