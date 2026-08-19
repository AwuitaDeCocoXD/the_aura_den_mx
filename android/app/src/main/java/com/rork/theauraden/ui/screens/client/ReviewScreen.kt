package com.rork.theauraden.ui.screens.client

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.Appointment
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDetailScaffold
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTextField
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraDivider
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

private val ratingLabels = listOf(
    "Cuéntanos cómo te fue",
    "No fue lo que esperaba",
    "Pudo estar mejor",
    "Estuvo bien",
    "Muy buen servicio",
    "¡Quedé encantada!"
)

private val quickTags = listOf(
    "Puntual",
    "Trato cálido",
    "Diseño impecable",
    "Espacio limpio",
    "Volvería"
)

/** The guest rates a finished visit. Visual only: nothing is published. */
@Composable
fun ReviewScreen(
    appointment: Appointment,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(setOf<String>()) }

    val specialist = DemoData.specialists
        .firstOrNull { it.name == appointment.specialistName }
        ?: DemoData.currentSpecialist

    AuraDetailScaffold(
        header = {
            AuraHeader(
                title = "Califica tu visita",
                eyebrow = appointment.dateLabel,
                subtitle = "${appointment.service} · ${appointment.specialistName}",
                onBack = onBack
            )
        },
        bottomAction = {
            Column {
                AuraPrimaryButton(
                    text = "Enviar reseña",
                    enabled = rating > 0,
                    onClick = onSubmit
                )
                TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Ahora no",
                        style = MaterialTheme.typography.labelLarge,
                        color = AuraInkMuted
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            AuraAvatar(
                imageUrl = specialist.imageUrl,
                name = specialist.name,
                size = 88
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = specialist.name,
                style = MaterialTheme.typography.headlineSmall,
                color = AuraNavy
            )
            Spacer(Modifier.height(4.dp))
            Eyebrow(specialist.specialty)

            Spacer(Modifier.height(26.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { value ->
                    RatingStar(
                        filled = value <= rating,
                        onClick = { rating = value }
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = ratingLabels[rating],
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (rating > 0) AuraNavy else AuraInkMuted,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(26.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    quickTags.filterIndexed { index, _ -> index % 2 == 0 }.forEach { tag ->
                        TagChip(
                            label = tag,
                            selected = tag in tags,
                            onClick = {
                                tags = if (tag in tags) tags - tag else tags + tag
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    quickTags.filterIndexed { index, _ -> index % 2 == 1 }.forEach { tag ->
                        TagChip(
                            label = tag,
                            selected = tag in tags,
                            onClick = {
                                tags = if (tag in tags) tags - tag else tags + tag
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            AuraTextField(
                label = "Cuéntale a otras invitadas (opcional)",
                value = comment,
                onValueChange = { comment = it },
                placeholder = "El diseño quedó justo como lo pedí…"
            )

            Spacer(Modifier.height(18.dp))
            AuraCard(containerColor = AuraCream, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Tu reseña ayuda a que otras invitadas encuentren a la especialista " +
                        "indicada para ellas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RatingStar(filled: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (filled) 1f else 0.86f,
        animationSpec = spring(dampingRatio = 0.42f, stiffness = 520f),
        label = "starScale"
    )
    val interaction = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(52.dp)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = if (filled) AuraYellow else AuraDivider,
            modifier = Modifier
                .size(44.dp)
                .scale(scale)
        )
    }
}

@Composable
private fun TagChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) AuraBlue else AuraWhite,
        border = BorderStroke(1.dp, if (selected) AuraBlue else AuraSandSoft)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(androidx.compose.ui.graphics.Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) AuraWhite else AuraNavy
            )
        }
    }
}

/** Compact prompt shown inside the history list for a visit that has no rating yet. */
@Composable
fun ReviewPromptCard(
    appointment: Appointment,
    onReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier, containerColor = AuraCream) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = AuraYellow,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "¿Cómo te fue con ${appointment.specialistName.split(" ").first()}?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraNavy
                )
                Text(
                    text = "${appointment.service} · ${appointment.dateLabel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted
                )
            }
            Spacer(Modifier.width(10.dp))
            TextButton(onClick = onReview) {
                Text(
                    text = "Calificar",
                    style = MaterialTheme.typography.labelLarge,
                    color = AuraBlue
                )
            }
        }
    }
}
