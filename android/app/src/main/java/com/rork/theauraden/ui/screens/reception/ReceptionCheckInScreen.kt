package com.rork.theauraden.ui.screens.reception

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HowToReg
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.data.CheckIn
import com.rork.theauraden.data.CheckInStatus
import com.rork.theauraden.data.DemoUiState
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraEmptyState
import com.rork.theauraden.ui.components.AuraFilterChip
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.CheckInStatusPill
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraBlueSoft
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft

private const val ALL = "Todas"
private const val PENDING = "Pendientes"
private const val ATTENDED = "Atendidas"
private val checkInFilters = listOf(ALL, PENDING, ATTENDED)

/** Arrival control for the front desk. */
@Composable
fun ReceptionCheckInScreen(
    state: DemoUiState,
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onMarkArrival: (String) -> Unit,
    onOpenWalkIn: () -> Unit
) {
    var filter by remember { mutableStateOf(ALL) }
    val visible = state.checkIns.filter { checkIn ->
        when (filter) {
            PENDING -> checkIn.status != CheckInStatus.ATTENDED
            ATTENDED -> checkIn.status == CheckInStatus.ATTENDED
            else -> true
        }
    }
    val nextArrival = state.checkIns.firstOrNull { it.status == CheckInStatus.PENDING }

    AuraTabScaffold(
        role = UserRole.RECEPTION,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                title = "Check-in de hoy",
                eyebrow = "Recepción",
                subtitle = AuraCopy.TODAY_LABEL
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 18.dp, bottom = 26.dp)
        ) {
            item {
                AuraCard(
                    containerColor = AuraBlueSoft,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${state.pendingCheckIns} citas restantes hoy",
                                style = MaterialTheme.typography.headlineSmall,
                                color = AuraNavy
                            )
                            if (nextArrival != null) {
                                Spacer(Modifier.height(5.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.Schedule,
                                        contentDescription = null,
                                        tint = AuraBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = "Próxima llegada · " +
                                            "${nextArrival.clientName} · ${nextArrival.time}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = AuraBlue
                                    )
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(AuraWhite, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.HowToReg,
                                contentDescription = null,
                                tint = AuraBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(checkInFilters) { option ->
                        AuraFilterChip(
                            label = option,
                            selected = option == filter,
                            onClick = { filter = option }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            if (visible.isEmpty()) {
                item {
                    AuraEmptyState(
                        title = if (filter == PENDING) {
                            "No tienes citas pendientes"
                        } else {
                            "No tienes citas hoy"
                        },
                        message = "Todas las clientas del día ya fueron registradas. " +
                            "Puedes dar de alta a una clienta sin cita.",
                        icon = Icons.Rounded.CheckCircle,
                        actionLabel = "Registrar walk-in",
                        onAction = onOpenWalkIn
                    )
                }
            } else {
                items(visible, key = { it.id }) { checkIn ->
                    CheckInCard(
                        checkIn = checkIn,
                        onMarkArrival = { onMarkArrival(checkIn.id) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }

            item {
                Spacer(Modifier.height(18.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CheckInStatusPill(CheckInStatus.PENDING)
                    CheckInStatusPill(CheckInStatus.WAITING)
                    CheckInStatusPill(CheckInStatus.ATTENDED)
                }
            }
        }
    }
}

@Composable
private fun CheckInCard(
    checkIn: CheckIn,
    onMarkArrival: () -> Unit,
    modifier: Modifier = Modifier
) {
    val attended = checkIn.status == CheckInStatus.ATTENDED
    AuraCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            if (attended) StatusGreenSoft else AuraSandSoft,
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = checkIn.clientName.split(" ")
                            .mapNotNull { it.firstOrNull() }
                            .joinToString(""),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (attended) StatusGreen else AuraNavy
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = checkIn.clientName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AuraInk
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = "${checkIn.service} · ${checkIn.specialistName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraInkMuted
                    )
                    Spacer(Modifier.height(6.dp))
                    Eyebrow(checkIn.time)
                }
                CheckInStatusPill(checkIn.status)
            }
            Spacer(Modifier.height(14.dp))
            if (attended) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = StatusGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(7.dp))
                    Text(
                        text = "Llegada registrada",
                        style = MaterialTheme.typography.labelLarge,
                        color = StatusGreen
                    )
                }
            } else {
                AuraSecondaryButton(text = "Marcar llegada", onClick = onMarkArrival)
            }
        }
    }
}
