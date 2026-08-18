package com.rork.theauraden.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.BodyFontFamily
import com.rork.theauraden.ui.theme.DisplayFontFamily
import com.rork.theauraden.ui.theme.EyebrowStyle
import com.rork.theauraden.ui.theme.LogoFontFamily

/** The simple brand arch that sits above the logotype. */
@Composable
fun AuraArc(
    modifier: Modifier = Modifier,
    color: Color = AuraYellow,
    strokeWidth: Dp = 2.dp
) {
    Canvas(modifier = modifier) {
        val stroke = strokeWidth.toPx()
        drawArc(
            color = color,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(stroke / 2f, stroke / 2f),
            size = Size(size.width - stroke, size.height * 2f - stroke),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

enum class LogoSize { Large, Medium, Compact }

/** "The Aura Den" logotype: script wordmark under the arch. */
@Composable
fun AuraLogo(
    modifier: Modifier = Modifier,
    size: LogoSize = LogoSize.Large,
    textColor: Color = AuraWhite,
    scriptColor: Color = AuraYellow,
    arcColor: Color = AuraYellow
) {
    val scriptSize = when (size) {
        LogoSize.Large -> 58.sp
        LogoSize.Medium -> 38.sp
        LogoSize.Compact -> 26.sp
    }
    val arcWidth = when (size) {
        LogoSize.Large -> 86.dp
        LogoSize.Medium -> 58.dp
        LogoSize.Compact -> 40.dp
    }
    val theSize = when (size) {
        LogoSize.Large -> 16.sp
        LogoSize.Medium -> 12.sp
        LogoSize.Compact -> 9.sp
    }
    val denSize = when (size) {
        LogoSize.Large -> 14.sp
        LogoSize.Medium -> 11.sp
        LogoSize.Compact -> 8.sp
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AuraArc(
            modifier = Modifier
                .width(arcWidth)
                .height(arcWidth / 2.6f),
            color = arcColor,
            strokeWidth = if (size == LogoSize.Large) 2.5.dp else 1.5.dp
        )
        Spacer(Modifier.height(if (size == LogoSize.Large) 10.dp else 4.dp))
        Text(
            text = "The",
            fontFamily = DisplayFontFamily,
            fontSize = theSize,
            color = textColor,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Aura",
            fontFamily = LogoFontFamily,
            fontSize = scriptSize,
            color = scriptColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = if (size == LogoSize.Large) 0.dp else 0.dp)
        )
        Text(
            text = "D E N",
            fontFamily = BodyFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = denSize,
            letterSpacing = denSize * 0.35f,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Brand header used at the top of every screen: deep blue block with soft rounded bottom,
 * optional back arrow and optional trailing action.
 */
@Composable
fun AuraHeader(
    title: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    containerColor: Color = AuraBlue,
    titleColor: Color = AuraWhite,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = containerColor,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 22.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Regresar",
                            tint = titleColor
                        )
                    }
                } else {
                    Spacer(Modifier.width(8.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    if (eyebrow != null) {
                        Text(
                            text = eyebrow.uppercase(),
                            style = EyebrowStyle,
                            color = AuraYellow
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = titleColor
                    )
                    if (subtitle != null) {
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = titleColor.copy(alpha = 0.82f)
                        )
                    }
                }
                if (trailing != null) {
                    Spacer(Modifier.width(12.dp))
                    trailing()
                }
            }
            if (content != null) {
                Spacer(Modifier.height(18.dp))
                Box(modifier = Modifier.padding(horizontal = 8.dp)) { content() }
            }
        }
    }
}

/** Small decorative arch + wordmark for card corners, matching the membership cards. */
@Composable
fun AuraCardMark(modifier: Modifier = Modifier, tint: Color = AuraBlue) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AuraArc(
            modifier = Modifier
                .width(34.dp)
                .height(13.dp),
            color = AuraYellow,
            strokeWidth = 1.5.dp
        )
        Spacer(Modifier.height(3.dp))
        Text(text = "The", fontFamily = DisplayFontFamily, fontSize = 9.sp, color = tint)
        Text(text = "Aura", fontFamily = LogoFontFamily, fontSize = 22.sp, color = tint)
        Text(
            text = "D E N",
            fontFamily = BodyFontFamily,
            fontSize = 7.sp,
            letterSpacing = 2.4.sp,
            color = tint
        )
    }
}


