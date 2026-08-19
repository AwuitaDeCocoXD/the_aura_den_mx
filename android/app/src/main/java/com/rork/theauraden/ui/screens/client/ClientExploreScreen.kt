package com.rork.theauraden.ui.screens.client

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
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
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.SpecialistProfile
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraFilterChip
import com.rork.theauraden.ui.components.AuraHeader
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.AuraTabScaffold
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.RatingStars
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraYellow

private val specialtyFilters = listOf("Todas", "Uñas", "Uñas acrílicas", "Pestañas / cejas")

/** The client browses specialists and picks who will attend her. */
@Composable
fun ClientExploreScreen(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onBack: () -> Unit,
    onBookWith: (specialistId: String) -> Unit
) {
    var filter by remember { mutableStateOf(specialtyFilters.first()) }
    val specialists = DemoData.specialists.filter {
        filter == "Todas" || it.specialty == filter
    }

    AuraTabScaffold(
        role = UserRole.CLIENT,
        currentRoute = currentRoute,
        onTabSelected = onTabSelected,
        header = {
            AuraHeader(
                onBack = onBack,
                title = "Explorar",
                eyebrow = "The Aura Den · ${AuraCopy.NEIGHBORHOOD}",
                subtitle = "Elige con quién quieres consentirte"
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
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(specialtyFilters) { option ->
                        AuraFilterChip(
                            label = option,
                            selected = option == filter,
                            onClick = { filter = option }
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
            }

            items(specialists, key = { it.id }) { specialist ->
                SpecialistCard(
                    specialist = specialist,
                    onBook = { onBookWith(specialist.id) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 7.dp)
                )
            }
        }
    }
}

@Composable
private fun SpecialistCard(
    specialist: SpecialistProfile,
    onBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AuraAvatar(
                    imageUrl = specialist.imageUrl,
                    name = specialist.name,
                    size = 72
                )
                Spacer(Modifier.width(15.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = specialist.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = AuraNavy
                    )
                    Spacer(Modifier.height(6.dp))
                    StatusPill(
                        label = specialist.specialty,
                        containerColor = AuraYellow,
                        contentColor = AuraNavy
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RatingStars(rating = specialist.rating, starSize = 14)
                        Spacer(Modifier.width(7.dp))
                        Text(
                            text = "${specialist.rating} · ${specialist.reviewCount} reseñas",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraInkMuted
                        )
                    }
                }
            }
            if (specialist.nextServiceLabel != null) {
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        tint = AuraBlue,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(7.dp))
                    Text(
                        text = specialist.nextServiceLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraBlue
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Eyebrow("Servicios desde $450 MXN", color = AuraInkMuted)
            Spacer(Modifier.height(14.dp))
            AuraPrimaryButton(text = "Agendar con ella", onClick = onBook)
        }
    }
}
