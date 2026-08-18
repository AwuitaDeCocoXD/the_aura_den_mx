package com.rork.theauraden.ui.screens.reception

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.rork.theauraden.data.StationStatus
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDropdownField
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.AuraTextField
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft

/** Quick intake for a client who arrives without an appointment. */
@Composable
fun ReceptionWalkInScreen(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onRegister: (
        clientName: String,
        service: String,
        specialist: String,
        station: String
    ) -> Unit
) {
    var clientName by remember { mutableStateOf("") }
    var service by remember { mutableStateOf(DemoData.services.first().name) }
    var specialist by remember { mutableStateOf(DemoData.specialists.first().name) }
    val freeStations = DemoData.stations.filter { it.status == StationStatus.AVAILABLE }
    var station by remember { mutableStateOf(freeStations.first().name) }

    AuraTabScaffold(
        role = UserRole.RECEPTION,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                title = "Registrar walk-in",
                eyebrow = "Recepción",
                subtitle = "Clienta sin cita previa"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(18.dp))
            AuraCard(containerColor = AuraCream) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                AuraSand.copy(alpha = 0.3f),
                                RoundedCornerShape(13.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Chair,
                            contentDescription = null,
                            tint = AuraNavy,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                    Spacer(Modifier.width(13.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${freeStations.size} estaciones libres ahora",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AuraNavy
                        )
                        Text(
                            text = freeStations.joinToString(" · ") { it.name },
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraInkMuted
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            AuraTextField(
                label = "Nombre de la clienta",
                value = clientName,
                onValueChange = { clientName = it },
                placeholder = "Valeria Núñez"
            )
            Spacer(Modifier.height(18.dp))
            AuraDropdownField(
                label = "Servicio",
                value = service,
                options = DemoData.services.map { it.name },
                onSelect = { service = it }
            )
            Spacer(Modifier.height(18.dp))
            AuraDropdownField(
                label = "Especialista disponible",
                value = specialist,
                options = DemoData.specialists.map { it.name },
                onSelect = { specialist = it }
            )
            Spacer(Modifier.height(18.dp))
            AuraDropdownField(
                label = "Estación",
                value = station,
                options = freeStations.map { it.name },
                onSelect = { station = it }
            )

            Spacer(Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusPill(
                    label = "Entra en espera",
                    containerColor = StatusGreenSoft,
                    contentColor = StatusGreen,
                    icon = Icons.Rounded.CheckCircle
                )
                Eyebrow("Se agrega al check-in de hoy")
            }

            Spacer(Modifier.height(22.dp))
            AuraPrimaryButton(
                text = "Dar de alta",
                onClick = { onRegister(clientName, service, specialist, station) }
            )
            Spacer(Modifier.height(26.dp))
        }
    }
}
