package com.rork.theauraden.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraNavyDeep
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow
import com.rork.theauraden.ui.theme.BodyFontFamily
import com.rork.theauraden.ui.theme.DisplayFontFamily
import com.rork.theauraden.ui.theme.LogoFontFamily
import kotlinx.coroutines.delay

/**
 * Opening animation: the brand arch draws itself, then the wordmark fades up.
 * Hands over to the welcome screen on its own after roughly two seconds.
 */
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val arcSweep = remember { Animatable(0f) }
    val wordmark = remember { Animatable(0f) }
    val tagline = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        arcSweep.animateTo(1f, tween(760, easing = LinearOutSlowInEasing))
        wordmark.animateTo(1f, tween(560))
        tagline.animateTo(1f, tween(420))
        delay(420)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(AuraBlue, AuraBlue, AuraNavyDeep))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Canvas(
                modifier = Modifier
                    .width(96.dp)
                    .height(38.dp)
            ) {
                val stroke = 2.6.dp.toPx()
                drawArc(
                    color = AuraYellow,
                    startAngle = 180f,
                    sweepAngle = 180f * arcSweep.value,
                    useCenter = false,
                    topLeft = Offset(stroke / 2f, stroke / 2f),
                    size = Size(size.width - stroke, size.height * 2f - stroke),
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "The",
                fontFamily = DisplayFontFamily,
                fontSize = 17.sp,
                color = AuraWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(wordmark.value)
            )
            Text(
                text = "Aura",
                fontFamily = LogoFontFamily,
                fontSize = 62.sp,
                color = AuraYellow,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(wordmark.value)
                    .padding(top = (10 * (1f - wordmark.value)).dp)
            )
            Text(
                text = "D E N",
                fontFamily = BodyFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                letterSpacing = 5.2.sp,
                color = AuraWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(wordmark.value)
            )
            Spacer(Modifier.height(26.dp))
            Text(
                text = AuraCopy.TAGLINE,
                fontFamily = BodyFontFamily,
                fontSize = 12.sp,
                letterSpacing = 2.4.sp,
                color = AuraWhite.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(tagline.value)
            )
        }
    }
}
