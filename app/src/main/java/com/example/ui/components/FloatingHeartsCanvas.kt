package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SoftPink
import kotlin.random.Random

data class RomanticFloatingParticle(
    val initialX: Float,
    val speed: Float,
    val size: Float,
    val alpha: Float,
    val color: Color
)

/**
 * Romantic Floating Ambient Particles Canvas:
 * Soft pink, rose, and blush ambient particles that drift gently upwards,
 * creating a dreamy, ethereal white & pink atmosphere.
 */
@Composable
fun FloatingHeartsCanvas(
    modifier: Modifier = Modifier,
    particleCount: Int = 14,
    primaryColor: Color = RomanticPink
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ambient_specks")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ambientProgress"
    )

    val particles = remember(primaryColor) {
        val random = Random(42)
        List(particleCount) {
            RomanticFloatingParticle(
                initialX = random.nextFloat(),
                speed = 0.4f + random.nextFloat() * 0.6f,
                size = 3f + random.nextFloat() * 4.5f,
                alpha = 0.15f + random.nextFloat() * 0.25f,
                color = if (random.nextBoolean()) primaryColor else SoftPink
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        particles.forEachIndexed { index, p ->
            val totalTravel = canvasHeight + 80f
            val currentY = canvasHeight - ((progress * p.speed * totalTravel + (index * 60f)) % totalTravel)
            val sway = kotlin.math.sin((progress * 6.28f + index).toDouble()).toFloat() * 14f
            val currentX = (p.initialX * canvasWidth + sway).coerceIn(0f, canvasWidth)

            // Draw soft romantic ambient glowing circle
            drawCircle(
                color = p.color.copy(alpha = p.alpha),
                radius = p.size,
                center = Offset(currentX, currentY)
            )
        }
    }
}
