package com.rork.theauraden.ui.screens.client

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.Appointment
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDangerTextButton
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.DeleteAccountDialog
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.InfoRow
import com.rork.theauraden.ui.components.MetricTile
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/** Client account screen: her data, her legal documents and account deletion. */
@Composable
fun ClientProfileScreen(
    upcoming: List<Appointment>,
    past: List<Appointment>,
    currentRoute: String,
    guestName: String,
    guestInitials: String,
    onTabSelected: (String) -> Unit,
    onBack: () -> Unit,
    onOpenLegal: () -> Unit,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,
    onOpenRoleSwitcher: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val completed = past.count { it.status.name == "COMPLETED" }

    AuraTabScaffold(
        role = UserRole.CLIENT,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                onBack = onBack,
                title = guestName,
                eyebrow = "Mi cuenta",
                subtitle = "Invitada de ${AuraCopy.BRAND_NAME}",
                trailing = {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(AuraWhite.copy(alpha = 0.16f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = guestInitials,
                            style = MaterialTheme.typography.titleMedium,
                            color = AuraYellow
                        )
                    }
                }
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
            Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
                MetricTile(
                    value = completed.toString(),
                    label = "Servicios recibidos",
                    modifier = Modifier.weight(1f),
                    containerColor = AuraCream
                )
                MetricTile(
                    value = upcoming.size.toString(),
                    label = "Citas próximas",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))
            AuraCard {
                Column {
                    InfoRow(
                        label = "Celular",
                        value = "+52 55 8899 3311",
                        icon = Icons.Rounded.Phone
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
                    InfoRow(
                        label = "Correo",
                        value = "lucia@correo.com",
                        icon = Icons.Rounded.Email
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            AuraCard(containerColor = AuraCream) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = null,
                        tint = AuraSand,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Eyebrow("Mi especialista favorita", color = AuraSand)
                        Spacer(Modifier.height(5.dp))
                        Text(
                            text = "Juanita Cruz",
                            style = MaterialTheme.typography.titleMedium,
                            color = AuraNavy
                        )
                    }
                    StatusPill(
                        label = "4 visitas",
                        containerColor = AuraYellow,
                        contentColor = AuraNavy
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            AuraCard {
                Column {
                    InfoRow(
                        label = "Legal",
                        value = "Términos y aviso de privacidad",
                        icon = Icons.Rounded.Description,
                        showChevron = true,
                        onClick = onOpenLegal
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
                    InfoRow(
                        label = "Modo demo",
                        value = "Cambiar de vista",
                        icon = Icons.Rounded.SwapHoriz,
                        showChevron = true,
                        onClick = onOpenRoleSwitcher
                    )
                }
            }

            Spacer(Modifier.height(22.dp))
            AuraSecondaryButton(text = "Cerrar sesión", onClick = onSignOut)
            Spacer(Modifier.height(6.dp))
            AuraDangerTextButton(
                text = "Eliminar mi cuenta",
                onClick = { showDeleteDialog = true }
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Al eliminar tu cuenta se borra tu historial de citas y tus datos " +
                    "personales de The Aura Den.",
                style = MaterialTheme.typography.bodySmall,
                color = AuraInkMuted,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = AuraCopy.BRAND_NAME,
                style = MaterialTheme.typography.bodySmall,
                color = AuraInk.copy(alpha = 0.35f)
            )
            Spacer(Modifier.height(26.dp))
        }
    }

    if (showDeleteDialog) {
        DeleteAccountDialog(
            accountName = AuraCopy.CLIENT_USER,
            onDismiss = { showDeleteDialog = false },
            onConfirmed = {
                showDeleteDialog = false
                onDeleteAccount()
            }
        )
    }
}
