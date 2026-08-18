package com.rork.theauraden.ui.screens.specialist

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.MembershipPlan
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraCardMark
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraSecondaryButton
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/** The three monthly memberships, with Residente as the recommended plan. */
@Composable
fun MembershipsScreen(
    activePlanId: String,
    onBack: () -> Unit,
    onChoosePlan: (String) -> Unit
) {
    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Membresías",
                eyebrow = "Tu espacio, tus reglas",
                subtitle = "Elige el ritmo que acompaña tu talento",
                onBack = onBack
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
            DemoData.plans.forEach { plan ->
                PlanCard(
                    plan = plan,
                    isActive = plan.id == activePlanId,
                    onChoose = { onChoosePlan(plan.id) },
                    modifier = Modifier.padding(bottom = 14.dp)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Puedes cambiar de plan cuando lo necesites.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuraInkMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun PlanCard(
    plan: MembershipPlan,
    isActive: Boolean,
    onChoose: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuraCard(
        modifier = modifier,
        containerColor = if (plan.recommended) AuraCream else AuraWhite,
        border = if (plan.recommended) BorderStroke(1.5.dp, AuraYellow) else null
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    if (plan.recommended) {
                        StatusPill(
                            label = "Recomendada",
                            containerColor = AuraYellow,
                            contentColor = AuraNavy,
                            icon = Icons.Rounded.Star
                        )
                        Spacer(Modifier.height(12.dp))
                    } else if (isActive) {
                        StatusPill(
                            label = "Tu plan actual",
                            containerColor = AuraBlue,
                            contentColor = AuraWhite
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    Eyebrow("Membresía")
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = plan.name,
                        style = MaterialTheme.typography.displaySmall,
                        color = AuraNavy
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$${"%,d".format(plan.price)}",
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
                if (plan.recommended) {
                    AuraCardMark(modifier = Modifier.width(56.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            plan.perks.forEach { perk ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = AuraBlue,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = perk,
                        style = MaterialTheme.typography.bodyLarge,
                        color = AuraInk,
                        fontWeight = if (plan.recommended) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            if (isActive) {
                AuraSecondaryButton(text = "Renovar este plan", onClick = onChoose)
            } else {
                AuraPrimaryButton(text = "Elegir plan", onClick = onChoose)
            }
        }
    }
}
