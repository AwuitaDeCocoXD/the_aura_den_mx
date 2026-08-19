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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Lock
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.Station
import com.rork.theauraden.data.StationStatus
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraFilterChip
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.StatusAmber
import com.rork.theauraden.ui.theme.StatusAmberSoft
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft
import com.rork.theauraden.ui.theme.StatusGrey
import com.rork.theauraden.ui.theme.StatusGreySoft

private val stationFilters = listOf("Todas", "Disponibles", "Mesa de uñas", "Pestañas")

/** Editorial discovery of the coworking stations. */
@Composable
fun SpacesScreen(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onBack: () -> Unit,
    hasSignedContract: Boolean,
    onOpenStation: (String) -> Unit,
    onOpenMemberships: () -> Unit,
    onSignContract: () -> Unit
) {
    var filter by remember { mutableStateOf(stationFilters.first()) }
    val stations = DemoData.stations.filter { station ->
        when (filter) {
            "Disponibles" -> station.status == StationStatus.AVAILABLE
            "Mesa de uñas" -> station.kind == "Mesa de uñas"
            "Pestañas" -> station.kind == "Pestañas"
            else -> true
        }
    }

    AuraTabScaffold(
        role = UserRole.SPECIALIST,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                onBack = onBack,
                title = "Explorar espacios",
                eyebrow = AuraCopy.TODAY_LABEL,
                subtitle = "Encuentra tu lugar para crear"
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp)
        ) {
            if (!hasSignedContract) {
                item {
                    ContractLockedCard(
                        onSignContract = onSignContract,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(Modifier.height(18.dp))
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(stationFilters) { option ->
                        AuraFilterChip(
                            label = option,
                            selected = option == filter,
                            onClick = { filter = option }
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
            }

            items(stations, key = { it.id }) { station ->
                StationCard(
                    station = station,
                    locked = !hasSignedContract,
                    onOpen = { if (hasSignedContract) onOpenStation(station.id) else onSignContract() },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 7.dp)
                )
            }

            item {
                Spacer(Modifier.height(14.dp))
                MembershipTeaserCard(
                    onOpenMemberships = onOpenMemberships,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

/** Renting is blocked until the rental agreement is signed. */
@Composable
private fun ContractLockedCard(
    onSignContract: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier, containerColor = StatusAmberSoft) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(StatusAmber.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = StatusAmber,
                        modifier = Modifier.size(19.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Falta firmar tu contrato",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = StatusAmber
                    )
                    Text(
                        text = "Necesario para reservar una estación",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            AuraPrimaryButton(text = "Firmar contrato de renta", onClick = onSignContract)
        }
    }
}

@Composable
private fun StationCard(
    station: Station,
    locked: Boolean,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val available = station.status == StationStatus.AVAILABLE && !locked
    AuraCard(modifier = modifier) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(168.dp)
                    .background(AuraSandSoft)
            ) {
                AsyncImage(
                    model = station.imageUrl,
                    contentDescription = station.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (available) 1f else 0.55f)
                )
                StatusPill(
                    label = when {
                        locked -> "Requiere contrato"
                        available -> "Disponible ahora"
                        else -> "Ocupada"
                    },
                    containerColor = if (available) StatusGreenSoft else StatusGreySoft,
                    contentColor = if (available) StatusGreen else StatusGrey,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                )
            }
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = AuraNavy
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        tint = if (available) AuraBlue else StatusGrey,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = station.scheduleLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (available) AuraBlue else StatusGrey,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (!available && station.nextAvailability != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = station.nextAvailability,
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
                Spacer(Modifier.height(10.dp))
                Eyebrow(station.amenities.joinToString(" · "))
                Spacer(Modifier.height(16.dp))
                when {
                    locked -> AuraSecondaryButton(
                        text = "Firmar contrato para reservar",
                        onClick = onOpen,
                        leadingIcon = Icons.Rounded.Lock
                    )
                    available -> AuraPrimaryButton(text = "Reservar espacio", onClick = onOpen)
                    else -> AuraSecondaryButton(text = "Ver detalles", onClick = onOpen)
                }
            }
        }
    }
}

@Composable
private fun MembershipTeaserCard(
    onOpenMemberships: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier, containerColor = AuraCream) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(AuraSand.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = AuraNavy,
                        modifier = Modifier.size(19.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = "¿Vienes varias veces al mes?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AuraInk
                    )
                    Text(
                        text = "Conoce la membresía Residente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraInkMuted
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            AuraSecondaryButton(text = "Ver membresías", onClick = onOpenMemberships)
        }
    }
}
