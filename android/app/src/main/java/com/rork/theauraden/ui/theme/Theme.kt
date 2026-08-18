package com.rork.theauraden.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val AuraColorScheme = lightColorScheme(
    primary = AuraBlue,
    onPrimary = AuraWhite,
    primaryContainer = AuraBlueSoft,
    onPrimaryContainer = AuraNavy,
    secondary = AuraSand,
    onSecondary = AuraNavyDeep,
    secondaryContainer = AuraSandSoft,
    onSecondaryContainer = AuraNavyDeep,
    tertiary = AuraYellow,
    onTertiary = AuraNavyDeep,
    tertiaryContainer = AuraYellow,
    onTertiaryContainer = AuraNavyDeep,
    background = AuraCanvas,
    onBackground = AuraInk,
    surface = AuraSurface,
    onSurface = AuraInk,
    surfaceVariant = AuraCream,
    onSurfaceVariant = AuraInkMuted,
    outline = AuraDivider,
    outlineVariant = AuraDivider,
    error = StatusRed,
    onError = AuraWhite,
    errorContainer = StatusRedSoft,
    onErrorContainer = StatusRed
)

private val AuraShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(CardCornerRadius),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AuraColorScheme,
        typography = AuraTypography,
        shapes = AuraShapes,
        content = content
    )
}
