package com.rork.theauraden.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.PriorityHigh
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
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.Station
import com.rork.theauraden.data.StationStatus
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraFilterChip
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.MetricTile
import com.rork.theauraden.ui.components.SectionHeading
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft
import com.rork.theauraden.ui.theme.StatusGrey
import com.rork.theauraden.ui.theme.StatusGreySoft
import com.rork.theauraden.ui.theme.StatusRed
import com.rork.theauraden.ui.theme.StatusRedSoft

private val liveFilters = listOf("Todas", "Disponibles", "En uso")

/** Owner's live floor: station status plus the day's key numbers. */
@Composable
fun AdminLiveScreen(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onOpenSpecialists: () -> Unit,
    onOpenRoleSwitcher: () -> Unit
) {
    val occupied = DemoData.stations.count { it.status == StationStatus.OCCUPIED }
    val overdueCount = DemoData.pendingCharges.count { it.overdue }
    var filter by remember { mutableStateOf(liveFilters.first()) }
    val stations = DemoData.stations.filter {
        when (filter) {
            "Disponibles" -> it.status == StationStatus.AVAILABLE
            "En uso" -> it.status == StationStatus.OCCUPIED
            else -> true
        }
    }

    AuraTabScaffold(
        role = UserRole.ADMIN,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                title = "Vista en vivo",
                eyebrow = "Modo administrador",
                subtitle = "${AuraCopy.TODAY_LABEL} · 11:42 am",
                trailing = {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(AuraWhite.copy(alpha = 0.14f), CircleShape)
                            .clickable(onClick = onOpenRoleSwitcher),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AD",
                            style = MaterialTheme.typography.titleSmall,
                            color = AuraYellow
                        )
                    }
                }
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
                PendingPaymentsBanner(
                    overdueCount = overdueCount,
                    total = DemoData.pendingTotal,
                    onClick = onOpenSpecialists,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(20.dp))

                SectionHeading(
                    text = "Piso en vivo",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$occupied de ${DemoData.stations.size} estaciones ocupadas ahora " +
                        "mismo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(14.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(liveFilters) { option ->
                        AuraFilterChip(
                            label = option,
                            selected = option == filter,
                            onClick = { filter = option }
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
            }

            items(stations.chunked(2)) { row ->
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { station ->
                        LiveStationTile(station = station, modifier = Modifier.weight(1f))
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }

            item {
                Spacer(Modifier.height(20.dp))
                SectionHeading(
                    text = "Resumen de hoy",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
                DemoData.todayKpis.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { (value, label) ->
                            MetricTile(
                                value = value,
                                label = label,
                                modifier = Modifier.weight(1f),
                                containerColor = if (label == "Ingresos del día") {
                                    AuraCream
                                } else {
                                    AuraWhite
                                }
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(AuraYellow, CircleShape)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Datos actualizados hace 1 minuto",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
            }
        }
    }
}

/** Red alert that pulls the owner straight into the renters screen. */
@Composable
private fun PendingPaymentsBanner(
    overdueCount: Int,
    total: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuraCard(
        modifier = modifier,
        containerColor = StatusRedSoft,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(StatusRed, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.PriorityHigh,
                    contentDescription = null,
                    tint = AuraWhite,
                    modifier = Modifier.size(21.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$overdueCount pagos vencidos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = StatusRed
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "$${"%,d".format(total)} por cobrar en total",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
            }
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = StatusRed
            )
        }
    }
}

@Composable
private fun LiveStationTile(station: Station, modifier: Modifier = Modifier) {
    val available = station.status == StationStatus.AVAILABLE
    AuraCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            if (available) StatusGreenSoft else AuraSandSoft,
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Chair,
                        contentDescription = null,
                        tint = if (available) StatusGreen else AuraNavy,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                StatusPill(
                    label = if (available) "Libre" else "Ocupada",
                    containerColor = if (available) StatusGreenSoft else StatusGreySoft,
                    contentColor = if (available) StatusGreen else StatusGrey
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = station.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AuraInk
            )
            Spacer(Modifier.height(6.dp))
            if (available) {
                Eyebrow("Sin reserva", color = AuraInkMuted)
            } else {
                Text(
                    text = station.occupiedBy ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraBlue
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    tint = AuraInkMuted,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = if (available) {
                        station.scheduleLabel
                    } else {
                        station.nextAvailability ?: station.scheduleLabel
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
            }
        }
    }
}
