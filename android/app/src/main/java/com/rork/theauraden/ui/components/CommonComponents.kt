package com.rork.theauraden.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rork.theauraden.data.AppointmentStatus
import com.rork.theauraden.data.CheckInStatus
import com.rork.theauraden.data.PaymentStatus
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraBlueSoft
import com.rork.theauraden.ui.theme.AuraCream
import com.rork.theauraden.ui.theme.AuraDivider
import com.rork.theauraden.ui.theme.AuraInk
import com.rork.theauraden.ui.theme.AuraInkFaint
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraSandSoft
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.EyebrowStyle
import com.rork.theauraden.ui.theme.StatusAmber
import com.rork.theauraden.ui.theme.StatusAmberSoft
import com.rork.theauraden.ui.theme.StatusGreen
import com.rork.theauraden.ui.theme.StatusGreenSoft
import com.rork.theauraden.ui.theme.StatusGrey
import com.rork.theauraden.ui.theme.StatusGreySoft
import com.rork.theauraden.ui.theme.StatusRed
import com.rork.theauraden.ui.theme.StatusRedSoft

/** Uppercase eyebrow label. */
@Composable
fun Eyebrow(text: String, modifier: Modifier = Modifier, color: Color = AuraInkMuted) {
    Text(text = text.uppercase(), style = EyebrowStyle, color = color, modifier = modifier)
}

/** Section heading used between blocks of content. */
@Composable
fun SectionHeading(
    text: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = AuraNavy,
            modifier = Modifier.weight(1f)
        )
        if (action != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Text(text = action, style = MaterialTheme.typography.labelLarge, color = AuraBlue)
            }
        }
    }
}

/** Soft rounded card, the base surface of the whole product. */
@Composable
fun AuraCard(
    modifier: Modifier = Modifier,
    containerColor: Color = AuraWhite,
    border: BorderStroke? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    val colors = CardDefaults.cardColors(containerColor = containerColor)
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) { content() }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) { content() }
    }
}

/** Status pill with sober brand-safe tones. */
@Composable
fun StatusPill(
    label: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Row(
        modifier = modifier
            .background(containerColor, CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(5.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}

@Composable
fun PaymentStatusPill(status: PaymentStatus, modifier: Modifier = Modifier) {
    val (container, content) = when (status) {
        PaymentStatus.PAID -> StatusGreenSoft to StatusGreen
        PaymentStatus.PENDING -> StatusAmberSoft to StatusAmber
        PaymentStatus.FAILED -> StatusRedSoft to StatusRed
    }
    StatusPill(status.label, container, content, modifier)
}

@Composable
fun CheckInStatusPill(status: CheckInStatus, modifier: Modifier = Modifier) {
    val (container, content) = when (status) {
        CheckInStatus.PENDING -> StatusGreySoft to StatusGrey
        CheckInStatus.WAITING -> AuraYellow to AuraNavy
        CheckInStatus.ATTENDED -> StatusGreenSoft to StatusGreen
    }
    StatusPill(status.label, container, content, modifier)
}

@Composable
fun AppointmentStatusPill(status: AppointmentStatus, modifier: Modifier = Modifier) {
    val (container, content) = when (status) {
        AppointmentStatus.CONFIRMED -> AuraYellow to AuraNavy
        AppointmentStatus.COMPLETED -> StatusGreenSoft to StatusGreen
        AppointmentStatus.CANCELLED -> StatusRedSoft to StatusRed
    }
    StatusPill(status.label, container, content, modifier)
}

/** Primary call to action: brand blue with white text. */
@Composable
fun AuraPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = AuraBlue,
    contentColor: Color = AuraWhite,
    leadingIcon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

/** Secondary action: blue outline, transparent fill. */
@Composable
fun AuraSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp),
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(1.5.dp, AuraBlue),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = AuraBlue)
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

/** Destructive action rendered as plain red text, never as a filled button. */
@Composable
fun AuraDangerTextButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Text(text = text, style = MaterialTheme.typography.labelLarge, color = StatusRed)
    }
}

/** Labelled row inside detail cards. */
@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    showChevron: Boolean = false
) {
    val base = if (onClick != null) modifier.clickable { onClick() } else modifier
    Row(
        modifier = base
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(AuraSandSoft, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AuraNavy,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = AuraInkMuted)
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AuraInk
            )
        }
        if (showChevron) {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = AuraInkFaint
            )
        }
    }
}

/** Horizontal date strip shared by reservation, agenda and scheduling flows. */
@Composable
fun DayPill(
    weekday: String,
    day: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasDot: Boolean = false
) {
    val container by animateColorAsState(
        targetValue = if (selected) AuraYellow else AuraWhite,
        animationSpec = tween(220),
        label = "dayContainer"
    )
    Column(
        modifier = modifier
            .width(62.dp)
            .background(container, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weekday,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) AuraNavy else AuraInkMuted
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = day,
            style = MaterialTheme.typography.titleLarge,
            color = if (selected) AuraNavy else AuraInk
        )
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .size(5.dp)
                .background(
                    color = when {
                        !hasDot -> Color.Transparent
                        selected -> AuraNavy
                        else -> AuraBlue
                    },
                    shape = CircleShape
                )
        )
    }
}

/** Selectable time slot chip. */
@Composable
fun TimeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val container = when {
        !enabled -> StatusGreySoft
        selected -> AuraYellow
        else -> AuraWhite
    }
    val content = when {
        !enabled -> AuraInkFaint
        selected -> AuraNavy
        else -> AuraInk
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = container,
        border = BorderStroke(1.dp, if (selected) AuraYellow else AuraDivider),
        onClick = { if (enabled) onClick() },
        enabled = enabled
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = content,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
        )
    }
}

/** Filter chip used in browse and operations screens. */
@Composable
fun AuraFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = if (selected) AuraBlue else AuraWhite,
        border = BorderStroke(1.dp, if (selected) AuraBlue else AuraDivider),
        onClick = onClick
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = if (selected) AuraWhite else AuraInkMuted,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

/** Animated hours progress bar for memberships. */
@Composable
fun HoursProgress(
    used: Int,
    total: Int,
    modifier: Modifier = Modifier,
    trackColor: Color = AuraSandSoft,
    color: Color = AuraBlue
) {
    val target = if (total == 0) 0f else (used.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    val animated by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 900),
        label = "hoursProgress"
    )
    LinearProgressIndicator(
        progress = { animated },
        modifier = modifier
            .fillMaxWidth()
            .height(9.dp),
        color = color,
        trackColor = trackColor,
        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        gapSize = 0.dp,
        drawStopIndicator = {}
    )
}

/** Star rating row. */
@Composable
fun RatingStars(
    rating: Double,
    modifier: Modifier = Modifier,
    starSize: Int = 16,
    tint: Color = AuraYellow
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = if (index < rating.toInt()) tint else AuraDivider,
                modifier = Modifier.size(starSize.dp)
            )
        }
    }
}

/** Circular photo used for specialists and clients. */
@Composable
fun AuraAvatar(
    imageUrl: String?,
    name: String,
    modifier: Modifier = Modifier,
    size: Int = 48,
    borderColor: Color = Color.Transparent
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .background(AuraSandSoft, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier
                    .size(size.dp)
                    .background(AuraSandSoft, CircleShape)
                    .clip(CircleShape)
            )
        } else {
            Text(
                text = name.split(" ").take(2).mapNotNull { it.firstOrNull() }.joinToString(""),
                style = MaterialTheme.typography.titleMedium,
                color = AuraNavy
            )
        }
        if (borderColor != Color.Transparent) {
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .background(Color.Transparent, CircleShape)
                    .border(2.dp, borderColor, CircleShape)
            )
        }
    }
}

/** Empty state with a light hand-drawn arch illustration. */
@Composable
fun AuraEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 34.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(AuraBlueSoft, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AuraArc(
                    modifier = Modifier
                        .width(46.dp)
                        .height(18.dp),
                    color = AuraBlue,
                    strokeWidth = 2.dp
                )
                if (icon != null) {
                    Spacer(Modifier.height(8.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AuraBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = AuraNavy,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = AuraInkMuted,
            textAlign = TextAlign.Center
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(20.dp))
            AuraSecondaryButton(
                text = actionLabel,
                onClick = onAction,
                modifier = Modifier.width(240.dp)
            )
        }
    }
}

/** Labelled text field matching the brand forms. */
@Composable
fun AuraTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    isPassword: Boolean = false,
    keyboardType: androidx.compose.ui.text.input.KeyboardType =
        androidx.compose.ui.text.input.KeyboardType.Text
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Eyebrow(label)
        Spacer(Modifier.height(7.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = AuraInkFaint
                    )
                }
            },
            singleLine = singleLine,
            minLines = minLines,
            visualTransformation = if (isPassword) {
                androidx.compose.ui.text.input.PasswordVisualTransformation()
            } else {
                androidx.compose.ui.text.input.VisualTransformation.None
            },
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = keyboardType
            ),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AuraBlue,
                unfocusedBorderColor = AuraDivider,
                focusedContainerColor = AuraWhite,
                unfocusedContainerColor = AuraWhite,
                focusedTextColor = AuraInk,
                unfocusedTextColor = AuraInk,
                cursorColor = AuraBlue
            )
        )
    }
}

/** Dropdown selector styled like the brand fields. */
@Composable
fun AuraDropdownField(
    label: String,
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        Eyebrow(label)
        Spacer(Modifier.height(7.dp))
        Box {
            Surface(
                onClick = { expanded = true },
                shape = RoundedCornerShape(16.dp),
                color = AuraWhite,
                border = BorderStroke(1.dp, AuraDivider),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = AuraInk,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                        tint = AuraInkMuted
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = AuraWhite
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (option == value) AuraBlue else AuraInk
                            )
                        },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/** Quick access tile used on the specialist dashboard. */
@Composable
fun QuickActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuraCard(modifier = modifier, onClick = onClick, containerColor = AuraWhite) {
        Column(modifier = Modifier.padding(18.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AuraBlue,
                modifier = Modifier.size(26.dp)
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AuraNavy
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = AuraInkMuted
            )
        }
    }
}

/** Compact metric tile for operations and reports. */
@Composable
fun MetricTile(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    caption: String? = null,
    captionColor: Color = AuraInkMuted,
    containerColor: Color = AuraWhite
) {
    AuraCard(modifier = modifier, containerColor = containerColor) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = AuraNavy
            )
            Spacer(Modifier.height(4.dp))
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = AuraInkMuted)
            if (caption != null) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = caption,
                    style = MaterialTheme.typography.labelSmall,
                    color = captionColor
                )
            }
        }
    }
}

/** Simple horizontal bar used in the admin reports screen. */
@Composable
fun BarRow(
    label: String,
    value: String,
    share: Float,
    modifier: Modifier = Modifier,
    barColor: Color = AuraBlue
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = AuraInk,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = AuraNavy
            )
        }
        Spacer(Modifier.height(8.dp))
        val animated by animateFloatAsState(
            targetValue = share.coerceIn(0f, 1f),
            animationSpec = tween(900),
            label = "barShare"
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(AuraCream, RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animated)
                    .height(10.dp)
                    .background(barColor, RoundedCornerShape(50))
            )
        }
    }
}

/** Vertical bar chart column for monthly revenue. */
@Composable
fun ChartColumn(
    label: String,
    share: Float,
    highlighted: Boolean,
    modifier: Modifier = Modifier
) {
    val animated by animateFloatAsState(
        targetValue = share.coerceIn(0.05f, 1f),
        animationSpec = tween(900),
        label = "chartColumn"
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(26.dp)
                .height((120 * animated).dp)
                .background(
                    color = if (highlighted) AuraBlue else AuraBlueSoft,
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (highlighted) AuraNavy else AuraInkMuted
        )
    }
}
