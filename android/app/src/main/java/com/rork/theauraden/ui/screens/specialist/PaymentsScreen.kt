package com.rork.theauraden.ui.screens.specialist

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.DemoUiState
import com.rork.theauraden.data.PaymentKind
import com.rork.theauraden.data.PaymentRecord
import com.rork.theauraden.data.PaymentStatus
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraCardMark
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.HoursProgress
import com.rork.theauraden.ui.components.PaymentStatusPill
import com.rork.theauraden.ui.components.SectionHeading
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft

/** Account status: current membership, hours used and payment history. */
@Composable
fun PaymentsScreen(
    state: DemoUiState,
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onBack: () -> Unit,
    onChangePlan: () -> Unit,
    onOpenReceipt: (String) -> Unit
) {
    val plan = state.activePlan
    val membershipPayments = state.payments.filter { it.kind == PaymentKind.MEMBERSHIP }
    val allPaid = membershipPayments.none { it.status == PaymentStatus.FAILED }

    AuraTabScaffold(
        role = UserRole.SPECIALIST,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                onBack = onBack,
                title = "Mis pagos",
                eyebrow = state.profile.name,
                content = {
                    StatusPill(
                        label = if (allPaid) "Todo al corriente" else "Revisa un cargo",
                        containerColor = StatusGreenSoft,
                        contentColor = StatusGreen,
                        icon = Icons.Rounded.CheckCircle
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 18.dp, bottom = 28.dp)
        ) {
            item {
                CurrentMembershipCard(
                    planName = plan.name,
                    price = plan.price,
                    hoursUsed = state.profile.hoursUsed,
                    totalHours = plan.hours,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(26.dp))
                SectionHeading(
                    text = "Historial de pagos",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(10.dp))
            }

            items(membershipPayments, key = { it.id }) { payment ->
                PaymentRow(
                    payment = payment,
                    onClick = { onOpenReceipt(payment.id) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }

            item {
                Spacer(Modifier.height(22.dp))
                AuraPrimaryButton(
                    text = "Cambiar de plan",
                    onClick = onChangePlan,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun CurrentMembershipCard(
    planName: String,
    price: Int,
    hoursUsed: Int,
    totalHours: Int,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier, containerColor = AuraCream) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Eyebrow("Membresía", color = AuraSand)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = planName,
                        style = MaterialTheme.typography.displaySmall,
                        color = AuraNavy
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$${"%,d".format(price)}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = AuraBlue
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "/ mes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraInkMuted,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                    }
                }
                AuraCardMark(modifier = Modifier.width(58.dp))
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = "$hoursUsed de $totalHours horas usadas",
                style = MaterialTheme.typography.titleMedium,
                color = AuraNavy
            )
            Spacer(Modifier.height(10.dp))
            HoursProgress(used = hoursUsed, total = totalHours)
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = null,
                    tint = AuraSand,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    text = "Próximo pago: 15 de septiembre",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AuraNavy
                )
            }
        }
    }
}

@Composable
private fun PaymentRow(
    payment: PaymentRecord,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(AuraSandSoft, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = null,
                    tint = AuraSand,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(13.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = payment.dateLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = AuraInk
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "$${"%,d".format(payment.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted
                )
            }
            PaymentStatusPill(payment.status)
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = AuraInkMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
