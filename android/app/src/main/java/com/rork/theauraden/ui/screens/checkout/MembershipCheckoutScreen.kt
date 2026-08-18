package com.rork.theauraden.ui.screens.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
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
import com.rork.theauraden.data.PaymentMethodOption
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraCardMark
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.SectionHeading
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraDivider
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraWhite

/** Membership charge: the specialist pays the coworking. Visual only, nothing is processed. */
@Composable
fun MembershipCheckoutScreen(
    planId: String,
    onBack: () -> Unit,
    onPay: (method: PaymentMethodOption, invoice: Boolean) -> Unit
) {
    val plan = DemoData.planById(planId)
    var methodId by remember { mutableStateOf(DemoData.paymentMethods.first().id) }
    var invoice by remember { mutableStateOf(false) }
    val method = DemoData.paymentMethods.first { it.id == methodId }

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Pagar membresía",
                eyebrow = "Renta del espacio",
                subtitle = "Plan ${plan.name} · mensual",
                onBack = onBack
            )
        },
        bottomAction = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = AuraInkMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Demo sin cobro real",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
                Spacer(Modifier.height(10.dp))
                AuraPrimaryButton(
                    text = "Pagar $${"%,d".format(plan.price)}",
                    onClick = { onPay(method, invoice) }
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
            Spacer(Modifier.height(18.dp))
            AuraCard(containerColor = AuraCream) {
                Row(modifier = Modifier.padding(20.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Eyebrow("Resumen del plan", color = AuraSand)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = plan.name,
                            style = MaterialTheme.typography.displaySmall,
                            color = AuraNavy
                        )
                        Spacer(Modifier.height(8.dp))
                        plan.perks.forEach { perk ->
                            Text(
                                text = "· $perk",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AuraInk
                            )
                        }
                    }
                    AuraCardMark(modifier = Modifier.width(54.dp))
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionHeading(text = "Método de pago")
            Spacer(Modifier.height(10.dp))
            DemoData.paymentMethods.forEach { option ->
                PaymentMethodRow(
                    option = option,
                    selected = option.id == methodId,
                    onSelect = { methodId = option.id }
                )
            }

            Spacer(Modifier.height(18.dp))
            AuraCard {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = invoice,
                        onCheckedChange = { invoice = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AuraBlue,
                            uncheckedColor = AuraDivider
                        )
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Solicitar factura (CFDI)",
                            style = MaterialTheme.typography.titleMedium,
                            color = AuraInk
                        )
                        Text(
                            text = "Se emite con tus datos fiscales registrados",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraInkMuted
                        )
                    }
                }
            }

            Spacer(Modifier.height(22.dp))
            AuraCard(containerColor = AuraWhite) {
                Column(modifier = Modifier.padding(18.dp)) {
                    TotalRow("Membresía ${plan.name}", "$${"%,d".format(plan.price)}")
                    Spacer(Modifier.height(8.dp))
                    TotalRow("Comisión de servicio", "$0")
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleLarge,
                            color = AuraNavy,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$${"%,d".format(plan.price)} MXN",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AuraBlue
                        )
                    }
                }
            }
            Spacer(Modifier.height(26.dp))
        }
    }
}

@Composable
internal fun PaymentMethodRow(
    option: PaymentMethodOption,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(18.dp),
        color = AuraWhite,
        border = BorderStroke(1.dp, if (selected) AuraBlue else AuraDivider),
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        if (selected) AuraBlue.copy(alpha = 0.1f) else AuraCream,
                        RoundedCornerShape(13.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (option.id) {
                        "spei" -> Icons.Rounded.AccountBalance
                        "mercadopago" -> Icons.Rounded.Wallet
                        else -> Icons.Rounded.CreditCard
                    },
                    contentDescription = null,
                    tint = if (selected) AuraBlue else AuraSand,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(13.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = AuraInk
                )
                Text(
                    text = option.detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
            }
            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = AuraBlue,
                    unselectedColor = AuraDivider
                )
            )
        }
    }
}

@Composable
internal fun TotalRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = AuraInkMuted,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = AuraInk
        )
    }
}
