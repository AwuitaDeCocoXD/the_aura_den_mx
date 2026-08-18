package com.rork.theauraden.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ContentCut
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraBlueSoft
import com.rork.theauraden.ui.theme.AuraDivider
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/**
 * Demo-only role switcher. Not part of the final product: it exists so the four
 * perspectives can be shown quickly in a client presentation without signing out.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSwitcherSheet(
    currentRole: UserRole,
    onSelect: (UserRole) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AuraWhite,
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(4.dp)
                        .background(AuraDivider, CircleShape)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Cambiar de vista",
                    style = MaterialTheme.typography.headlineSmall,
                    color = AuraNavy,
                    modifier = Modifier.weight(1f)
                )
                StatusPill("Solo demo", AuraYellow, AuraNavy)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Atajo para la presentación: cambia de perspectiva sin cerrar sesión.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuraInkMuted
            )
            Spacer(Modifier.height(18.dp))
            UserRole.entries.forEach { role ->
                val selected = role == currentRole
                Surface(
                    onClick = { onSelect(role) },
                    shape = RoundedCornerShape(18.dp),
                    color = if (selected) AuraBlueSoft else AuraWhite,
                    border = BorderStroke(1.dp, if (selected) AuraBlue else AuraDivider),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    if (selected) AuraBlue else AuraSandSoft,
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (role) {
                                    UserRole.SPECIALIST -> Icons.Rounded.ContentCut
                                    UserRole.CLIENT -> Icons.Rounded.Person
                                    UserRole.RECEPTION -> Icons.Rounded.SupportAgent
                                    UserRole.ADMIN -> Icons.Rounded.AdminPanelSettings
                                },
                                contentDescription = null,
                                tint = if (selected) AuraWhite else AuraNavy,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = role.label,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AuraInk
                            )
                            Text(
                                text = role.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraInkMuted
                            )
                        }
                        if (selected) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = AuraBlue
                            )
                        }
                    }
                }
            }
        }
    }
}
