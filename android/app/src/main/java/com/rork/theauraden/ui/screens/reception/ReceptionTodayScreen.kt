package com.rork.theauraden.ui.screens.reception

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.HowToReg
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.data.CheckInStatus
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.DemoUiState
import com.rork.theauraden.data.Station
import com.rork.theauraden.data.StationStatus
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.MetricTile
import com.rork.theauraden.ui.components.SectionHeading
import com.rork.theauraden.ui.components.StatusPill
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

/** Floor view for the front desk: who is working where, right now. */
@Composable
fun ReceptionTodayScreen(
    state: DemoUiState,
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onOpenCheckIn: () -> Unit,
    onOpenWalkIn: () -> Unit,
    onOpenRoleSwitcher: () -> Unit
) {
    val waiting = state.checkIns.count { it.status == CheckInStatus.WAITING }
    val pending = state.checkIns.count { it.status == CheckInStatus.PENDING }

    AuraTabScaffold(
        role = UserRole.RECEPTION,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                title = "Piso hoy",
                eyebrow = "Recepción",
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
                            text = "RC",
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
            contentPadding = PaddingValues(top = 18.dp, bottom = 26.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricTile(
                        value = "$waiting",
                        label = "En espera",
                        modifier = Modifier.weight(1f),
                        containerColor = AuraYellow
                    )
                    MetricTile(
                        value = "$pending",
                        label = "Por llegar",
                        modifier = Modifier.weight(1f)
                    )
                    MetricTile(
                        value = "6",
                        label = "Especialistas",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(24.dp))
                SectionHeading(
                    text = "Estaciones",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(10.dp))
                DemoData.stations.forEach { station ->
                    StationRow(
                        station = station,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                    )
                }
                Spacer(Modifier.height(22.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    AuraSecondaryButton(
                        text = "Ir al check-in",
                        onClick = onOpenCheckIn,
                        leadingIcon = Icons.Rounded.HowToReg
                    )
                    Spacer(Modifier.height(10.dp))
                    AuraSecondaryButton(
                        text = "Registrar walk-in",
                        onClick = onOpenWalkIn,
                        leadingIcon = Icons.Rounded.PersonAddAlt1
                    )
                }
            }
        }
    }
}

@Composable
private fun StationRow(station: Station, modifier: Modifier = Modifier) {
    val available = station.status == StationStatus.AVAILABLE
    AuraCard(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        if (available) StatusGreenSoft else AuraSandSoft,
                        RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Chair,
                    contentDescription = null,
                    tint = if (available) StatusGreen else AuraNavy,
                    modifier = Modifier.size(21.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraInk
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = if (available) {
                        station.scheduleLabel
                    } else {
                        "${station.occupiedBy} · ${station.scheduleLabel.lowercase()}"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
            }
            StatusPill(
                label = if (available) "Libre" else "En uso",
                containerColor = if (available) StatusGreenSoft else StatusGreySoft,
                contentColor = if (available) StatusGreen else StatusGrey
            )
        }
    }
}
