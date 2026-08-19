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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.PlanRevenue
import com.rork.theauraden.data.TopSpecialist
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.BarRow
import com.rork.theauraden.ui.components.ChartColumn
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.MetricTile
import com.rork.theauraden.ui.components.SectionHeading
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSand
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusRed

/** Monthly business report for the owner. */
@Composable
fun AdminReportsScreen(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val recurringRent = DemoData.revenueByPlan.sumOf { it.amount }

    AuraTabScaffold(
        role = UserRole.ADMIN,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                onBack = onBack,
                title = "Reportes",
                eyebrow = "Modo administrador",
                subtitle = "Agosto 2025 · cierre parcial"
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
                DemoData.monthMetrics.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { metric ->
                            MetricTile(
                                value = metric.value,
                                label = metric.label,
                                caption = metric.delta,
                                captionColor = if (metric.positive) StatusGreen else StatusRed,
                                containerColor = if (metric.label == "Ingresos del mes") {
                                    AuraCream
                                } else {
                                    AuraWhite
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            item {
                Spacer(Modifier.height(22.dp))
                SectionHeading(
                    text = "Tendencia de ingresos",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Últimos 6 meses",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
                AuraCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Eyebrow("Agosto")
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    text = "$186,400",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = AuraNavy
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .background(
                                        StatusGreen.copy(alpha = 0.12f),
                                        RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.TrendingUp,
                                    contentDescription = null,
                                    tint = StatusGreen,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text = "+89% vs marzo",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = StatusGreen
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            DemoData.revenueByMonth.forEachIndexed { index, month ->
                                ChartColumn(
                                    label = month.month,
                                    share = month.share,
                                    highlighted = index == DemoData.revenueByMonth.lastIndex,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(14.dp))
                        Text(
                            text = "Seis meses consecutivos al alza. Agosto cerró en " +
                                "$186,400 MXN, el mejor mes del año.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraInkMuted
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(22.dp))
                SectionHeading(
                    text = "Ingresos por membresía",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Renta fija mensual · $${"%,d".format(recurringRent)} MXN",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
                AuraCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        DemoData.revenueByPlan.forEachIndexed { index, plan ->
                            PlanRevenueRow(plan)
                            if (index != DemoData.revenueByPlan.lastIndex) {
                                Spacer(Modifier.height(18.dp))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(22.dp))
                SectionHeading(
                    text = "Top especialistas",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Más citas atendidas en agosto",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraInkMuted,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
                DemoData.topSpecialists.forEach { specialist ->
                    TopSpecialistRow(
                        specialist = specialist,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                    )
                }
            }

            item {
                Spacer(Modifier.height(22.dp))
                SectionHeading(
                    text = "Servicios más solicitados",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
                AuraCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        DemoData.serviceDemand.forEachIndexed { index, demand ->
                            BarRow(
                                label = demand.service,
                                value = "${demand.count} citas",
                                share = demand.share
                            )
                            if (index != DemoData.serviceDemand.lastIndex) {
                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(22.dp))
                AuraCard(
                    containerColor = AuraCream,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Eyebrow("Ocupación por horario", color = AuraSand)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "La tarde es tu hora pico",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AuraNavy
                        )
                        Spacer(Modifier.height(14.dp))
                        BarRow(label = "10:00 am – 1:00 pm", value = "64%", share = 0.64f)
                        Spacer(Modifier.height(14.dp))
                        BarRow(label = "1:00 pm – 4:00 pm", value = "88%", share = 0.88f)
                        Spacer(Modifier.height(14.dp))
                        BarRow(label = "4:00 pm – 8:00 pm", value = "72%", share = 0.72f)
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun PlanRevenueRow(plan: PlanRevenue) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Plan ${plan.planName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraInk
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${plan.specialists} especialistas",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
            }
            Text(
                text = "$${"%,d".format(plan.amount)}",
                style = MaterialTheme.typography.titleMedium,
                color = AuraNavy
            )
        }
        Spacer(Modifier.height(9.dp))
        BarRow(
            label = "",
            value = "",
            share = plan.share,
            barColor = when (plan.planName) {
                "Anchor" -> AuraNavy
                "Turista" -> AuraSand
                else -> AuraBlue
            }
        )
    }
}

@Composable
private fun TopSpecialistRow(specialist: TopSpecialist, modifier: Modifier = Modifier) {
    val isFirst = specialist.position == 1
    AuraCard(
        modifier = modifier,
        containerColor = if (isFirst) AuraCream else AuraWhite
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.BottomStart) {
                AuraAvatar(
                    imageUrl = specialist.imageUrl,
                    name = specialist.name,
                    size = 56
                )
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(
                            if (isFirst) AuraYellow else AuraWhite,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = specialist.position.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AuraNavy
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = specialist.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AuraInk
                    )
                    if (isFirst) {
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Rounded.EmojiEvents,
                            contentDescription = null,
                            tint = AuraSand,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = specialist.specialty,
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
                Spacer(Modifier.height(9.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .background(AuraSandSoft, RoundedCornerShape(50))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(specialist.share)
                            .height(7.dp)
                            .background(
                                if (isFirst) AuraBlue else AuraBlue.copy(alpha = 0.55f),
                                RoundedCornerShape(50)
                            )
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = specialist.appointments.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = AuraNavy
                )
                Text(
                    text = "citas",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$${"%,d".format(specialist.revenue)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = AuraBlue
                )
            }
        }
    }
}
