package com.rork.theauraden.ui.screens.specialist

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Draw
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.SignedContract
import com.rork.theauraden.data.SpecialistProfile
import com.rork.theauraden.ui.components.AuraAvatar
import com.rork.theauraden.ui.components.AuraCard
import com.rork.theauraden.ui.components.AuraDangerTextButton
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.DeleteAccountDialog
import com.rork.theauraden.ui.components.Eyebrow
import com.rork.theauraden.ui.components.InfoRow
import com.rork.theauraden.ui.components.RatingStars
import com.rork.theauraden.ui.components.StatusPill
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCanvas
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

/** Specialist profile with contact data, reviews and the discreet demo role shortcut. */
@Composable
fun ProfileScreen(
    profile: SpecialistProfile,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    contract: SignedContract?,
    onOpenContract: () -> Unit,
    onOpenLegal: () -> Unit,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,
    onOpenRoleSwitcher: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(containerColor = AuraCanvas) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        AuraBlue,
                        RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(bottom = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Regresar",
                                tint = AuraWhite
                            )
                        }
                        Text(
                            text = "Perfil",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AuraWhite,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onOpenRoleSwitcher) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "Más opciones",
                                tint = AuraWhite
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(136.dp)
                            .background(AuraWhite, CircleShape)
                            .padding(4.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AuraAvatar(
                            imageUrl = profile.imageUrl,
                            name = profile.name,
                            size = 128
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.displaySmall,
                        color = AuraWhite
                    )
                    Spacer(Modifier.height(10.dp))
                    StatusPill(
                        label = profile.specialty,
                        containerColor = AuraYellow,
                        contentColor = AuraNavy,
                        icon = Icons.Rounded.Star
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = profile.since,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraWhite.copy(alpha = 0.85f)
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(20.dp))
                AuraCard {
                    Column {
                        InfoRow(
                            label = "Celular",
                            value = profile.phone,
                            icon = Icons.Rounded.Phone,
                            showChevron = true,
                            onClick = onEditProfile
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
                        InfoRow(
                            label = "Correo",
                            value = profile.email,
                            icon = Icons.Rounded.Email,
                            showChevron = true,
                            onClick = onEditProfile
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                AuraCard(containerColor = AuraCream) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Eyebrow("Mis reseñas", color = AuraSand)
                            Spacer(Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                                tint = AuraSand
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = profile.rating.toString(),
                            style = MaterialTheme.typography.displayMedium,
                            color = AuraNavy
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RatingStars(rating = profile.rating, starSize = 20)
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "${profile.reviewCount} reseñas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AuraInkMuted
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                DemoData.reviews.take(2).forEach { review ->
                    AuraCard(modifier = Modifier.padding(bottom = 10.dp)) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(AuraSandSoft, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = review.author.split(" ")
                                        .mapNotNull { it.firstOrNull() }
                                        .joinToString(""),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = AuraNavy
                                )
                            }
                            Spacer(Modifier.width(13.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = review.author,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AuraInk
                                )
                                Spacer(Modifier.height(3.dp))
                                RatingStars(rating = review.rating.toDouble(), starSize = 14)
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = "“${review.comment}”",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AuraInk
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = review.timeAgo,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraInkMuted
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))
                AuraCard(containerColor = AuraWhite, onClick = onOpenRoleSwitcher) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SwapHoriz,
                            contentDescription = null,
                            tint = AuraBlue
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cambiar de vista",
                                style = MaterialTheme.typography.titleMedium,
                                color = AuraInk
                            )
                            Text(
                                text = "Modo demo · Especialista, Clienta, Recepción, Admin",
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraInkMuted
                            )
                        }
                        StatusPill("Demo", AuraYellow, AuraNavy)
                    }
                }

                Spacer(Modifier.height(12.dp))
                AuraCard(
                    containerColor = if (contract == null) StatusAmberSoft else AuraWhite,
                    onClick = onOpenContract
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    if (contract == null) {
                                        StatusAmber.copy(alpha = 0.16f)
                                    } else {
                                        StatusGreenSoft
                                    },
                                    RoundedCornerShape(13.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (contract == null) {
                                    Icons.Rounded.Draw
                                } else {
                                    Icons.Rounded.Verified
                                },
                                contentDescription = null,
                                tint = if (contract == null) StatusAmber else StatusGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (contract == null) {
                                    "Firmar mi contrato"
                                } else {
                                    "Ver mi contrato firmado"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AuraInk
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = contract?.let {
                                    "Folio ${it.folio} · ${it.dateLabel}"
                                } ?: "Pendiente de firma",
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraInkMuted
                            )
                        }
                        StatusPill(
                            label = if (contract == null) "Pendiente" else "Firmado",
                            containerColor = if (contract == null) {
                                StatusAmber.copy(alpha = 0.16f)
                            } else {
                                StatusGreenSoft
                            },
                            contentColor = if (contract == null) StatusAmber else StatusGreen
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                AuraCard {
                    InfoRow(
                        label = "Legal",
                        value = "Términos y aviso de privacidad",
                        icon = Icons.Rounded.Description,
                        showChevron = true,
                        onClick = onOpenLegal
                    )
                }

                Spacer(Modifier.height(22.dp))
                AuraPrimaryButton(text = "Editar perfil", onClick = onEditProfile)
                Spacer(Modifier.height(6.dp))
                AuraDangerTextButton(text = "Cerrar sesión", onClick = onSignOut)
                Spacer(Modifier.height(2.dp))
                AuraDangerTextButton(
                    text = "Eliminar mi cuenta",
                    onClick = { showDeleteDialog = true }
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Al eliminar tu cuenta pierdes tu perfil, tus reseñas, tu historial " +
                        "de citas y tu membresía activa.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraInkMuted,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }

    if (showDeleteDialog) {
        DeleteAccountDialog(
            accountName = profile.name,
            onDismiss = { showDeleteDialog = false },
            onConfirmed = {
                showDeleteDialog = false
                onDeleteAccount()
            }
        )
    }
}
