package com.rork.theauraden.ui.screens.admin

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Draw
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Verified
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.PaymentStatus
import com.rork.theauraden.data.PendingCharge
import com.rork.theauraden.data.SpecialistProfile
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraFilterChip
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.HoursProgress
import com.rork.theauraden.ui.components.SectionHeading
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.StatusAmber
import com.rork.theauraden.ui.theme.StatusAmberSoft
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft
import com.rork.theauraden.ui.theme.StatusRed
import com.rork.theauraden.ui.theme.StatusRedSoft

private val paymentFilters = listOf("Todas", "Al corriente", "Con adeudo")

/** Roster of renters: plan, payment health and hours consumed, plus what is still owed. */
@Composable
fun AdminSpecialistsScreen(
    currentRoute: String,
    onTabSelected: (String) -> Unit
) {
    var filter by remember { mutableStateOf(paymentFilters.first()) }
    val specialists = DemoData.specialists.filter {
        when (filter) {
            "Al corriente" -> it.paymentStatus == PaymentStatus.PAID
            "Con adeudo" -> it.paymentStatus != PaymentStatus.PAID
            else -> true
        }
    }
    val monthlyIncome = DemoData.specialists.sumOf { DemoData.planById(it.planId).price }

    AuraTabScaffold(
        role = UserRole.ADMIN,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                title = "Rentistas",
                eyebrow = "Modo administrador",
                subtitle = "${DemoData.specialists.size} especialistas activas",
                content = {
                    StatusPill(
                        label = "Renta comprometida $${"%,d".format(monthlyIncome)} / mes",
                        containerColor = AuraYellow,
                        contentColor = AuraNavy
                    )
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
                PendingPaymentsSection(modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(Modifier.height(24.dp))
                SectionHeading(
                    text = "Todas las rentistas",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(paymentFilters) { option ->
                        AuraFilterChip(
                            label = option,
                            selected = option == filter,
                            onClick = { filter = option }
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            items(specialists, key = { it.id }) { specialist ->
                SpecialistRow(
                    specialist = specialist,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/** Highlighted block with everything the coworking still has to collect this month. */
@Composable
private fun PendingPaymentsSection(modifier: Modifier = Modifier) {
    val overdue = DemoData.pendingCharges.filter { it.overdue }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(StatusRedSoft, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.PriorityHigh,
                    contentDescription = null,
                    tint = StatusRed,
                    modifier = Modifier.size(17.dp)
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Pagos pendientes",
                style = MaterialTheme.typography.titleLarge,
                color = AuraNavy,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AmountTile(
                amount = DemoData.overdueTotal,
                label = "Vencido",
                caption = "${overdue.size} rentistas",
                containerColor = StatusRedSoft,
                valueColor = StatusRed,
                modifier = Modifier.weight(1f)
            )
            AmountTile(
                amount = DemoData.pendingTotal - DemoData.overdueTotal,
                label = "Por vencer",
                caption = "Esta semana",
                containerColor = AuraCream,
                valueColor = AuraNavy,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(12.dp))
        AuraCard {
            Column {
                DemoData.pendingCharges.forEachIndexed { index, charge ->
                    PendingChargeRow(charge)
                    if (index != DemoData.pendingCharges.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AmountTile(
    amount: Int,
    label: String,
    caption: String,
    containerColor: androidx.compose.ui.graphics.Color,
    valueColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier, containerColor = containerColor) {
        Column(modifier = Modifier.padding(16.dp)) {
            Eyebrow(label, color = valueColor)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "$${"%,d".format(amount)}",
                style = MaterialTheme.typography.headlineMedium,
                color = valueColor
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = AuraInkMuted
            )
        }
    }
}

@Composable
private fun PendingChargeRow(charge: PendingCharge) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (charge.overdue) StatusRedSoft else AuraSandSoft,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = charge.specialistName.split(" ")
                    .take(2)
                    .mapNotNull { it.firstOrNull() }
                    .joinToString(""),
                style = MaterialTheme.typography.labelLarge,
                color = if (charge.overdue) StatusRed else AuraNavy
            )
        }
        Spacer(Modifier.width(13.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = charge.specialistName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AuraInk
            )
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Membresía ${charge.planName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(AuraInkMuted, CircleShape)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = charge.dueLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (charge.overdue) StatusRed else StatusAmber
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${"%,d".format(charge.amount)}",
                style = MaterialTheme.typography.titleMedium,
                color = if (charge.overdue) StatusRed else AuraNavy
            )
            Spacer(Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.NotificationsActive,
                    contentDescription = null,
                    tint = AuraBlue,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Recordar",
                    style = MaterialTheme.typography.labelSmall,
                    color = AuraBlue
                )
            }
        }
    }
}

/** Contract health of a renter: signed with folio, or still pending signature. */
@Composable
private fun ContractStatusRow(specialist: SpecialistProfile) {
    val signed = specialist.contractSigned
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (signed) StatusGreenSoft.copy(alpha = 0.6f) else StatusAmberSoft,
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 13.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (signed) Icons.Rounded.Verified else Icons.Rounded.Draw,
            contentDescription = null,
            tint = if (signed) StatusGreen else StatusAmber,
            modifier = Modifier.size(17.dp)
        )
        Spacer(Modifier.width(9.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (signed) "Contrato firmado" else "Pendiente de firma",
                style = MaterialTheme.typography.labelLarge,
                color = if (signed) StatusGreen else StatusAmber
            )
            Text(
                text = if (signed) {
                    "Folio ${specialist.contractFolio.orEmpty()} · " +
                        specialist.contractDateLabel.orEmpty()
                } else {
                    "No puede reservar estaciones hasta firmar"
                },
                style = MaterialTheme.typography.bodySmall,
                color = AuraInkMuted
            )
        }
    }
}

@Composable
private fun SpecialistRow(
    specialist: SpecialistProfile,
    modifier: Modifier = Modifier
) {
    val plan = DemoData.planById(specialist.planId)
    val owes = specialist.paymentStatus != PaymentStatus.PAID
    val hoursShare = if (plan.hours == 0) {
        0f
    } else {
        specialist.hoursUsed.toFloat() / plan.hours.toFloat()
    }

    AuraCard(
        modifier = modifier,
        border = if (owes) {
            androidx.compose.foundation.BorderStroke(1.dp, StatusRed.copy(alpha = 0.45f))
        } else {
            null
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AuraAvatar(
                    imageUrl = specialist.imageUrl,
                    name = specialist.name,
                    size = 54
                )
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = specialist.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = AuraInk
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = specialist.specialty,
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
                StatusPill(
                    label = if (owes) "Con adeudo" else "Al corriente",
                    containerColor = if (owes) StatusRedSoft else StatusGreenSoft,
                    contentColor = if (owes) StatusRed else StatusGreen,
                    icon = if (owes) Icons.Rounded.PriorityHigh else null
                )
            }

            Spacer(Modifier.height(14.dp))
            ContractStatusRow(specialist = specialist)

            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(AuraSandSoft, RoundedCornerShape(11.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CreditCard,
                        contentDescription = null,
                        tint = AuraNavy,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(11.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Eyebrow("Plan ${plan.name}")
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = if (owes) {
                            "Adeuda $${"%,d".format(plan.price)} de este mes"
                        } else {
                            "Pagado este mes"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (owes) StatusRed else AuraInkMuted
                    )
                }
                Text(
                    text = "$${"%,d".format(plan.price)} / mes",
                    style = MaterialTheme.typography.titleSmall,
                    color = AuraBlue
                )
            }

            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    tint = AuraInkMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Horas del plan",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${specialist.hoursUsed} / ${plan.hours} h",
                    style = MaterialTheme.typography.labelLarge,
                    color = AuraNavy
                )
            }
            Spacer(Modifier.height(9.dp))
            HoursProgress(
                used = specialist.hoursUsed,
                total = plan.hours,
                color = when {
                    hoursShare >= 0.9f -> StatusAmber
                    owes -> StatusRed
                    else -> AuraBlue
                }
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = when {
                    hoursShare >= 0.9f -> "Casi agota su plan, buen momento para subirla de nivel"
                    hoursShare <= 0.5f -> "Usa menos de la mitad de sus horas"
                    else -> "Ritmo de uso saludable"
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (hoursShare >= 0.9f) StatusAmber else AuraInkMuted
            )
        }
    }
}
