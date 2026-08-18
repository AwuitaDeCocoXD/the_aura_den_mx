package com.rork.theauraden.ui.screens.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.AuraCopy
import com.rork.theauraden.ui.components.AuraLogo
import com.rork.theauraden.ui.components.AuraPrimaryButton
import com.rork.theauraden.ui.components.LogoSize
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraNavyDeep
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/** Brand entrance: full-bleed blue canvas, centered logotype and two large actions. */
@Composable
fun WelcomeScreen(
    onCreateAccount: () -> Unit,
    onSignIn: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(AuraBlue, AuraBlue, AuraNavyDeep)
                )
            )
    ) {
        BrandTexture(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            AuraLogo(size = LogoSize.Large)
            Spacer(Modifier.height(20.dp))
            Text(
                text = AuraCopy.TAGLINE,
                style = MaterialTheme.typography.labelMedium,
                color = AuraWhite.copy(alpha = 0.72f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.weight(1f))
            AuraPrimaryButton(
                text = "Crear cuenta",
                onClick = onCreateAccount,
                containerColor = AuraWhite,
                contentColor = AuraBlue
            )
            Spacer(Modifier.height(14.dp))
            AuraPrimaryButton(
                text = "Iniciar sesión",
                onClick = onSignIn,
                containerColor = AuraWhite.copy(alpha = 0.14f),
                contentColor = AuraWhite
            )
            Spacer(Modifier.height(26.dp))
            Text(
                text = "Un espacio para hacer crecer tu talento",
                style = MaterialTheme.typography.bodyMedium,
                color = AuraYellow.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(28.dp))
        }
    }
}

/** Very subtle geometric texture inspired by the brand board. */
@Composable
private fun BrandTexture(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val step = size.width / 7f
        val dot = 2.2f
        var y = step * 0.6f
        while (y < size.height) {
            var x = step * 0.5f
            while (x < size.width) {
                drawCircle(
                    color = AuraNavy.copy(alpha = 0.35f),
                    radius = dot,
                    center = Offset(x, y)
                )
                x += step
            }
            y += step
        }
    }
}
