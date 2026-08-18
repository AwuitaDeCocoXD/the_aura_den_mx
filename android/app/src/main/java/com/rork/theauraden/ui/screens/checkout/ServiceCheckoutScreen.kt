package com.rork.theauraden.ui.screens.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.rork.theauraden.data.ClientService
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.PaymentMethodOption
import com.rork.theauraden.data.SpecialistProfile
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.RatingStars
import com.rork.theauraden.ui.components.SectionHeading
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraDivider
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraWhite

/**
 * Service charge: the client pays her specialist through the app. Separate money flow from
 * the membership checkout, even though both share the brand's checkout language.
 */
@Composable
fun ServiceCheckoutScreen(
    specialist: SpecialistProfile,
    service: ClientService,
    onBack: () -> Unit,
    onPay: (method: PaymentMethodOption, invoice: Boolean) -> Unit
) {
    var methodId by remember { mutableStateOf(DemoData.paymentMethods.first().id) }
    var invoice by remember { mutableStateOf(false) }
    val method = DemoData.paymentMethods.first { it.id == methodId }
    val serviceFee = 0

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Pagar servicio",
                eyebrow = "Tu cita",
                subtitle = "${service.name} con ${specialist.name}",
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
                    text = "Pagar $${"%,d".format(service.price)}",
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
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AuraAvatar(
                        imageUrl = specialist.imageUrl,
                        name = specialist.name,
                        size = 62
                    )
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Eyebrow("Con", color = AuraSand)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = specialist.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = AuraNavy
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RatingStars(rating = specialist.rating, starSize = 13)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = specialist.rating.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraInkMuted
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            AuraCard(containerColor = AuraWhite) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Eyebrow("Servicio")
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = AuraNavy
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            tint = AuraBlue,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(7.dp))
                        Text(
                            text = "${service.durationLabel} · Martes 18 de agosto, 10:00 am",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraBlue
                        )
                    }
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
                            text = "Te la enviamos a tu correo",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraInkMuted
                        )
                    }
                }
            }

            Spacer(Modifier.height(22.dp))
            AuraCard(containerColor = AuraWhite) {
                Column(modifier = Modifier.padding(18.dp)) {
                    TotalRow(service.name, "$${"%,d".format(service.price)}")
                    Spacer(Modifier.height(8.dp))
                    TotalRow("Cargo por reservar", "$$serviceFee")
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
                            text = "$${"%,d".format(service.price + serviceFee)} MXN",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AuraBlue
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "El pago va directo a ${specialist.name}; The Aura Den solo " +
                            "procesa el cobro.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraInkMuted
                    )
                }
            }
            Spacer(Modifier.height(26.dp))
        }
    }
}
