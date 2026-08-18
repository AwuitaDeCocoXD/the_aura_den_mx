package com.rork.theauraden.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rork.theauraden.ui.theme.AuraNavy

/**
 * Finger drawing surface used to capture the specialist's signature.
 * Strokes are kept as plain point lists so the parent screen owns the state and can clear it.
 */
@Composable
fun SignaturePad(
    strokes: List<List<Offset>>,
    onStrokeFinished: (List<Offset>) -> Unit,
    modifier: Modifier = Modifier,
    inkColor: Color = AuraNavy,
    strokeWidth: Dp = 3.dp
) {
    var current by remember { mutableStateOf<List<Offset>>(emptyList()) }

    LaunchedEffect(strokes) {
        if (strokes.isEmpty()) current = emptyList()
    }

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset -> current = listOf(offset) },
                onDrag = { change, _ ->
                    change.consume()
                    current = current + change.position
                },
                onDragEnd = {
                    if (current.size > 1) onStrokeFinished(current)
                    current = emptyList()
                },
                onDragCancel = { current = emptyList() }
            )
        }
    ) {
        val width = strokeWidth.toPx()
        strokes.forEach { points -> drawSignatureStroke(points, inkColor, width) }
        drawSignatureStroke(current, inkColor, width)
    }
}

private fun DrawScope.drawSignatureStroke(points: List<Offset>, color: Color, width: Float) {
    if (points.size < 2) {
        points.firstOrNull()?.let { drawCircle(color, width / 2f, it) }
        return
    }
    val path = Path().apply {
        moveTo(points.first().x, points.first().y)
        points.drop(1).forEach { lineTo(it.x, it.y) }
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(width = width, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}
