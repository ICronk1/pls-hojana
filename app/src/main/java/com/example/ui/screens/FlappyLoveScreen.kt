package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.AppConfig
import com.example.data.AppPreferences
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.KeyGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SoftCream
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SubtleGrayText
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground
import kotlin.math.sin
import kotlin.random.Random

data class FlappyPipe(
    var x: Float,
    val topHeight: Float,
    val gap: Float,
    var passed: Boolean = false
)

data class HeartScoreParticle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    var life: Float = 1f,
    val size: Float = 10f
)

data class Cloud(
    var x: Float,
    val y: Float,
    val width: Float,
    val speed: Float
)

data class Building(
    val x: Float,
    val width: Float,
    val height: Float
)

@Composable
fun FlappyLoveScreen(
    appPreferences: AppPreferences,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val playerBitmap = ImageBitmap.imageResource(id = R.drawable.img_flappy_player)
    var gameState by remember { mutableStateOf("START") } // "START", "PLAYING", "GAMEOVER"
    var score by remember { mutableIntStateOf(0) }
    var highScore by remember { mutableIntStateOf(appPreferences.getFlappyHighScore()) }

    // Screen dimensions state from BoxWithConstraints
    var canvasWidth by remember { mutableFloatStateOf(1080f) }
    var canvasHeight by remember { mutableFloatStateOf(1920f) }

    // Physics parameters (scaled to frame delta)
    var birdY by remember { mutableFloatStateOf(600f) }
    var birdVelocity by remember { mutableFloatStateOf(0f) }
    var birdRotation by remember { mutableFloatStateOf(0f) }
    var wingFlapPhase by remember { mutableFloatStateOf(0f) }
    var groundOffset by remember { mutableFloatStateOf(0f) }

    val pipes = remember { mutableStateListOf<FlappyPipe>() }
    val scoreParticles = remember { mutableStateListOf<HeartScoreParticle>() }

    // Background scenery elements
    val clouds = remember {
        mutableStateListOf(
            Cloud(x = 100f, y = 140f, width = 160f, speed = 0.5f),
            Cloud(x = 450f, y = 220f, width = 200f, speed = 0.35f),
            Cloud(x = 800f, y = 110f, width = 140f, speed = 0.6f)
        )
    }

    // Classic Flappy Physics tuning
    val gravity = 0.72f
    val jumpImpulse = -12.5f
    val pipeSpeed = 5.2f
    val pipeWidth = 130f
    val pipeGap = 290f // Fair yet challenging gap for mobile screens
    val groundHeight = 130f

    fun triggerVibration(durationMs: Long = 35) {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(durationMs)
                }
            }
        } catch (e: Exception) {
            // Safe ignore
        }
    }

    fun jump() {
        if (gameState == "START") {
            gameState = "PLAYING"
            birdY = canvasHeight * 0.42f
            birdVelocity = jumpImpulse
            birdRotation = -25f
            score = 0
            pipes.clear()
            scoreParticles.clear()
            triggerVibration(25)
        } else if (gameState == "PLAYING") {
            birdVelocity = jumpImpulse
            birdRotation = -28f
            triggerVibration(20)
        }
    }

    fun restart() {
        gameState = "PLAYING"
        birdY = canvasHeight * 0.42f
        birdVelocity = jumpImpulse
        birdRotation = -25f
        score = 0
        pipes.clear()
        scoreParticles.clear()
        triggerVibration(30)
    }

    // MAIN FLAPPY BIRD ENGINE LOOP
    LaunchedEffect(gameState, canvasWidth, canvasHeight) {
        if (gameState == "PLAYING") {
            var lastPipeSpawn = System.currentTimeMillis()
            var lastNanos = 0L

            while (gameState == "PLAYING") {
                withFrameNanos { frameNanos ->
                    if (lastNanos == 0L) lastNanos = frameNanos
                    val deltaSec = ((frameNanos - lastNanos) / 1_000_000_000f).coerceIn(0.008f, 0.033f)
                    lastNanos = frameNanos
                    val timeScale = deltaSec / 0.0166f // Normalized to ~60FPS

                    // 1. Gravity & Position
                    birdVelocity += gravity * timeScale
                    birdY += birdVelocity * timeScale

                    // 2. Realistic Rotation physics (tilts up on flap, dives down as falling)
                    if (birdVelocity < 0) {
                        birdRotation = (birdRotation - 8f * timeScale).coerceAtLeast(-30f)
                    } else {
                        birdRotation = (birdRotation + 4.5f * timeScale).coerceAtMost(70f)
                    }

                    // 3. Wing Flap animation
                    wingFlapPhase = (wingFlapPhase + 0.35f * timeScale) % (2f * Math.PI.toFloat())

                    // 4. Ground scrolling
                    groundOffset = (groundOffset + pipeSpeed * timeScale) % 40f

                    // 5. Cloud drifting
                    clouds.forEach { cloud ->
                        cloud.x -= cloud.speed * timeScale
                        if (cloud.x + cloud.width < 0) {
                            cloud.x = canvasWidth + 50f
                        }
                    }

                    // 6. Spawn Classic Flappy Pipes
                    val now = System.currentTimeMillis()
                    val spawnInterval = 1750L // Classic steady cadence
                    if (now - lastPipeSpawn > spawnInterval && canvasHeight > 400f) {
                        val minTop = 140f
                        val maxTop = (canvasHeight - groundHeight - pipeGap - 140f).coerceAtLeast(minTop + 60f)
                        val topHeight = minTop + Random.nextFloat() * (maxTop - minTop)
                        pipes.add(
                            FlappyPipe(
                                x = canvasWidth + 20f,
                                topHeight = topHeight,
                                gap = pipeGap
                            )
                        )
                        lastPipeSpawn = now
                    }

                    // 7. Pipe Movement, Collision & Scoring
                    val birdX = canvasWidth * 0.28f
                    val birdRadius = 26f
                    val birdPlayableBottom = canvasHeight - groundHeight

                    // Ground hit or sky limit
                    if (birdY + birdRadius >= birdPlayableBottom) {
                        birdY = birdPlayableBottom - birdRadius
                        gameState = "GAMEOVER"
                        triggerVibration(120)
                    } else if (birdY - birdRadius <= 10f) {
                        birdY = 10f + birdRadius
                        birdVelocity = 1f
                    }

                    val pipeIterator = pipes.iterator()
                    while (pipeIterator.hasNext()) {
                        val pipe = pipeIterator.next()
                        pipe.x -= pipeSpeed * timeScale

                        // Score trigger when passing pipe center
                        if (!pipe.passed && (pipe.x + pipeWidth * 0.5f) < birdX) {
                            pipe.passed = true
                            score += 1
                            triggerVibration(40)

                            // Generate festive score heart particles
                            repeat(6) {
                                scoreParticles.add(
                                    HeartScoreParticle(
                                        x = birdX,
                                        y = birdY,
                                        vx = (Random.nextFloat() - 0.5f) * 8f,
                                        vy = -Random.nextFloat() * 7f - 3f,
                                        size = 12f + Random.nextFloat() * 8f
                                    )
                                )
                            }
                        }

                        // Precise AABB / Circle Collision
                        val pipeLeft = pipe.x
                        val pipeRight = pipe.x + pipeWidth
                        val topPipeBottom = pipe.topHeight
                        val bottomPipeTop = pipe.topHeight + pipe.gap

                        if (birdX + birdRadius > pipeLeft && birdX - birdRadius < pipeRight) {
                            // Check if bird is OUTSIDE the safe gap
                            val insideGap = (birdY - birdRadius > topPipeBottom) &&
                                    (birdY + birdRadius < bottomPipeTop)
                            if (!insideGap) {
                                gameState = "GAMEOVER"
                                triggerVibration(120)
                            }
                        }

                        // Remove offscreen pipes
                        if (pipe.x + pipeWidth < -40f) {
                            pipeIterator.remove()
                        }
                    }

                    // 8. Update floating particles
                    val particleIter = scoreParticles.iterator()
                    while (particleIter.hasNext()) {
                        val p = particleIter.next()
                        p.life -= 0.04f * timeScale
                        if (p.life <= 0f) {
                            particleIter.remove()
                        }
                    }
                }
            }

            // Save High Score on Game Over
            if (score > highScore) {
                highScore = score
                appPreferences.saveFlappyScore(score)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground),
        contentAlignment = Alignment.Center
    ) {
        FloatingHeartsCanvas(particleCount = 14, primaryColor = RomanticPink)

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 540.dp)
                .fillMaxWidth()
                .border(1.dp, SurfaceCardBorder)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (gameState == "PLAYING" || gameState == "START") {
                        jump()
                    }
                }
        ) {
        canvasWidth = constraints.maxWidth.toFloat()
        canvasHeight = constraints.maxHeight.toFloat()

        // Canvas Rendering
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cWidth = size.width
            val cHeight = size.height
            val birdX = cWidth * 0.28f
            val birdRadius = 26f
            val groundY = cHeight - groundHeight

            // 1. SKY GRADIENT (Romantic Soft Rose & White Horizon)
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF7F9), // Pure rose white
                        Color(0xFFFFEFF4), // Very soft blush
                        Color(0xFFFFE0EB), // Romantic pastel pink
                        Color(0xFFFFD4E2)  // Horizon pink glow
                    ),
                    startY = 0f,
                    endY = groundY
                ),
                size = Size(cWidth, groundY)
            )

            // 2. BACKGROUND CITY SILHOUETTES / DISTANT HILLS
            drawDistantCityscape(cWidth, groundY)

            // 3. FLUFFY CLOUDS
            clouds.forEach { cloud ->
                drawSoftCloud(cloud.x, cloud.y, cloud.width)
            }

            // 4. CLASSIC RETRO PIPES (Top & Bottom with lips/collars)
            pipes.forEach { pipe ->
                drawClassicFlappyPipe(
                    x = pipe.x,
                    topHeight = pipe.topHeight,
                    gap = pipe.gap,
                    groundY = groundY,
                    pipeWidth = pipeWidth
                )
            }

            // 5. MOVING GROUND (Checkerboard / Striped retro earth)
            drawClassicGround(cWidth, cHeight, groundY, groundOffset)

            // 6. DRAW PLAYER BOX WITH PHOTO INSIDE
            val currentBirdY = if (gameState == "START") {
                // Gentle idle floating on start screen
                cHeight * 0.42f + sin(System.currentTimeMillis() / 250.0).toFloat() * 14f
            } else {
                birdY
            }

            rotate(
                degrees = if (gameState == "START") 0f else birdRotation,
                pivot = Offset(birdX, currentBirdY)
            ) {
                drawPhotoBox(
                    centerX = birdX,
                    centerY = currentBirdY,
                    boxWidth = birdRadius * 2.2f,
                    boxHeight = birdRadius * 2.2f,
                    playerBitmap = playerBitmap
                )
            }

            // 7. SCORE HEART PARTICLES
            scoreParticles.forEach { p ->
                drawHeartParticle(p.x, p.y, p.size * p.life, p.life)
            }
        }

        // TOP HUD: LARGE FLAPPY SCOREBOARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, start = 20.dp, end = 20.dp)
        ) {
            // Left: High Score
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F0818).copy(alpha = 0.75f))
                        .border(1.dp, Color(0xFF382247), RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "BEST: $highScore",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SoftPink
                    )
                }
            }

            // Center: Prominent Classic Big Flappy Score Display
            if (gameState == "PLAYING") {
                Text(
                    text = "$score",
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.TopCenter),
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color(0xFF000000),
                            offset = Offset(3f, 4f),
                            blurRadius = 6f
                        )
                    )
                )
            }
        }

        // START SCREEN OVERLAY
        AnimatedVisibility(
            visible = gameState == "START",
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CrispWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfacePinkLight)
                            .border(2.dp, RomanticPink, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_flappy_player),
                            contentDescription = "Player Photo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Flappy Box",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Fly your box through classic pipes and test your reflexes. Tap anywhere to jump and reach for a new high score!",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                    )

                    Button(
                        onClick = { jump() },
                        colors = ButtonDefaults.buttonColors(containerColor = RomanticPink, contentColor = CrispWhite),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("flappy_start_btn")
                    ) {
                        Text(
                            text = "Tap Screen or Here To Fly",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CrispWhite
                        )
                    }
                }
            }
        }

        // GAME OVER OVERLAY (CLASSIC ARCADE SCORECARD)
        AnimatedVisibility(
            visible = gameState == "GAMEOVER",
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(0.88f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CrispWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "GAME OVER",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Retro Medal / Score Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfacePinkLight)
                            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Medal icon based on score
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val medalEmoji = when {
                                    score >= 20 -> "🥇"
                                    score >= 10 -> "🥈"
                                    score >= 5 -> "🥉"
                                    else -> "🐣"
                                }
                                val medalTitle = when {
                                    score >= 20 -> "Platinum"
                                    score >= 10 -> "Gold"
                                    score >= 5 -> "Silver"
                                    else -> "Flight"
                                }
                                Text(text = medalEmoji, fontSize = 36.sp)
                                Text(text = medalTitle, fontSize = 11.sp, color = TextSecondary)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "SCORE", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                Text(text = "$score", fontSize = 28.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "BEST", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                Text(text = "$highScore", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = KeyGold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { restart() },
                        colors = ButtonDefaults.buttonColors(containerColor = RomanticPink, contentColor = CrispWhite),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("flappy_restart_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Restart", tint = CrispWhite)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Play Again", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = CrispWhite)
                    }
                }
            }
        }
    }
}
}

// ============================================================================
// DRAW SCOPE CUSTOM PAINTERS (PIPES, BIRD, GROUND, SCENERY)
// ============================================================================

/**
 * Renders the player as a typical box containing the photo inside,
 * complete with framed borders, smooth rounded corners, and a subtle glowing aura.
 */
private fun DrawScope.drawPhotoBox(
    centerX: Float,
    centerY: Float,
    boxWidth: Float,
    boxHeight: Float,
    playerBitmap: ImageBitmap
) {
    val halfW = boxWidth / 2f
    val halfH = boxHeight / 2f
    val boxLeft = centerX - halfW
    val boxTop = centerY - halfH
    val cornerRadius = CornerRadius(12f, 12f)

    // 1. Subtle glowing halo around the box
    drawRoundRect(
        color = RomanticPink.copy(alpha = 0.35f),
        topLeft = Offset(boxLeft - 4f, boxTop - 4f),
        size = Size(boxWidth + 8f, boxHeight + 8f),
        cornerRadius = CornerRadius(16f, 16f)
    )

    // 2. White card base
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(boxLeft, boxTop),
        size = Size(boxWidth, boxHeight),
        cornerRadius = cornerRadius
    )

    // 3. Center-cropped photo inside the box with rounded clipping
    val srcW = playerBitmap.width
    val srcH = playerBitmap.height
    val targetAspect = boxWidth / boxHeight
    val srcAspect = srcW.toFloat() / srcH.toFloat()
    val (cropW, cropH) = if (srcAspect > targetAspect) {
        Pair((srcH * targetAspect).toInt(), srcH)
    } else {
        Pair(srcW, (srcW / targetAspect).toInt())
    }
    val cropX = (srcW - cropW) / 2
    val cropY = (srcH - cropH) / 2

    clipPath(Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(Offset(boxLeft, boxTop), Size(boxWidth, boxHeight)),
                cornerRadius = cornerRadius
            )
        )
    }) {
        drawImage(
            image = playerBitmap,
            srcOffset = IntOffset(cropX, cropY),
            srcSize = IntSize(cropW, cropH),
            dstOffset = IntOffset(boxLeft.toInt(), boxTop.toInt()),
            dstSize = IntSize(boxWidth.toInt(), boxHeight.toInt())
        )
    }

    // 4. Outer Box Border (dark retro outline)
    drawRoundRect(
        color = Color(0xFF2B1B26),
        topLeft = Offset(boxLeft, boxTop),
        size = Size(boxWidth, boxHeight),
        cornerRadius = cornerRadius,
        style = Stroke(width = 3.5f)
    )

    // 5. Inner Accent Border (delicate pink rim)
    drawRoundRect(
        color = RomanticPink.copy(alpha = 0.85f),
        topLeft = Offset(boxLeft + 2.5f, boxTop + 2.5f),
        size = Size(boxWidth - 5f, boxHeight - 5f),
        cornerRadius = CornerRadius(9f, 9f),
        style = Stroke(width = 1.5f)
    )

    // 6. Cute decorative heart pin on top-right corner
    drawCircle(
        color = RomanticPink,
        radius = 5f,
        center = Offset(boxLeft + boxWidth - 8f, boxTop + 8f)
    )
}

/**
 * Draws the real Flappy Bird green pipes with highlights, shadows,
 * and thick collars (lips) at the top and bottom entrances.
 */
private fun DrawScope.drawClassicFlappyPipe(
    x: Float,
    topHeight: Float,
    gap: Float,
    groundY: Float,
    pipeWidth: Float
) {
    val collarExtraWidth = 18f
    val collarHeight = 36f
    val collarX = x - collarExtraWidth / 2f
    val collarWidth = pipeWidth + collarExtraWidth

    val pipeBodyBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF43A047), // Darker green on left edge
            Color(0xFF81C784), // Bright highlight stripe
            Color(0xFF4CAF50), // Standard green
            Color(0xFF2E7D32), // Deep shadow on right
            Color(0xFF1B5E20)  // Dark shadow
        ),
        startX = x,
        endX = x + pipeWidth
    )

    val collarBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF43A047),
            Color(0xFFA5D6A7),
            Color(0xFF4CAF50),
            Color(0xFF2E7D32),
            Color(0xFF1B5E20)
        ),
        startX = collarX,
        endX = collarX + collarWidth
    )

    val outlineColor = Color(0xFF103612)
    val outlineStroke = 3.5f

    // ----------------- TOP PIPE -----------------
    if (topHeight > collarHeight) {
        // Main stem
        val stemHeight = topHeight - collarHeight
        drawRect(
            brush = pipeBodyBrush,
            topLeft = Offset(x, 0f),
            size = Size(pipeWidth, stemHeight)
        )
        drawRect(
            color = outlineColor,
            topLeft = Offset(x, 0f),
            size = Size(pipeWidth, stemHeight),
            style = Stroke(width = outlineStroke)
        )

        // Collar at the end of top pipe
        drawRoundRect(
            brush = collarBrush,
            topLeft = Offset(collarX, stemHeight),
            size = Size(collarWidth, collarHeight),
            cornerRadius = CornerRadius(6f, 6f)
        )
        drawRoundRect(
            color = outlineColor,
            topLeft = Offset(collarX, stemHeight),
            size = Size(collarWidth, collarHeight),
            cornerRadius = CornerRadius(6f, 6f),
            style = Stroke(width = outlineStroke)
        )
    }

    // ----------------- BOTTOM PIPE -----------------
    val bottomPipeY = topHeight + gap
    val bottomPipeHeight = (groundY - bottomPipeY).coerceAtLeast(0f)

    if (bottomPipeHeight > collarHeight) {
        // Collar at the entrance of bottom pipe
        drawRoundRect(
            brush = collarBrush,
            topLeft = Offset(collarX, bottomPipeY),
            size = Size(collarWidth, collarHeight),
            cornerRadius = CornerRadius(6f, 6f)
        )
        drawRoundRect(
            color = outlineColor,
            topLeft = Offset(collarX, bottomPipeY),
            size = Size(collarWidth, collarHeight),
            cornerRadius = CornerRadius(6f, 6f),
            style = Stroke(width = outlineStroke)
        )

        // Main stem down to the ground
        val bottomStemY = bottomPipeY + collarHeight
        val bottomStemHeight = bottomPipeHeight - collarHeight
        drawRect(
            brush = pipeBodyBrush,
            topLeft = Offset(x, bottomStemY),
            size = Size(pipeWidth, bottomStemHeight)
        )
        drawRect(
            color = outlineColor,
            topLeft = Offset(x, bottomStemY),
            size = Size(pipeWidth, bottomStemHeight),
            style = Stroke(width = outlineStroke)
        )
    }
}

/**
 * Draws the scrolling textured ground with retro green grass strip,
 * diagonal dirt slashes, and dark soil bedrock.
 */
private fun DrawScope.drawClassicGround(
    width: Float,
    height: Float,
    groundY: Float,
    groundOffset: Float
) {
    val groundH = height - groundY

    // Dirt Bedrock Base
    drawRect(
        brush = Brush.verticalGradient(
            listOf(Color(0xFFDED895), Color(0xFFC8B36D), Color(0xFF795548)),
            startY = groundY,
            endY = height
        ),
        topLeft = Offset(0f, groundY),
        size = Size(width, groundH)
    )

    // Top Green Grass Strip
    val grassHeight = 18f
    drawRect(
        color = Color(0xFF73BF2E),
        topLeft = Offset(0f, groundY),
        size = Size(width, grassHeight)
    )
    drawRect(
        color = Color(0xFF558B2F),
        topLeft = Offset(0f, groundY + grassHeight - 4f),
        size = Size(width, 4f)
    )
    // Dark separator line
    drawLine(
        color = Color(0xFF2E4C14),
        start = Offset(0f, groundY),
        end = Offset(width, groundY),
        strokeWidth = 3f
    )

    // Scrolling diagonal dirt stripes
    val stripeSpacing = 32f
    var startX = -stripeSpacing + (groundOffset % stripeSpacing)
    while (startX < width + stripeSpacing) {
        val path = Path().apply {
            moveTo(startX, groundY + grassHeight + 2f)
            lineTo(startX + 14f, groundY + grassHeight + 2f)
            lineTo(startX - 6f, groundY + grassHeight + 22f)
            lineTo(startX - 20f, groundY + grassHeight + 22f)
            close()
        }
        drawPath(path, color = Color(0xFF9E8E52).copy(alpha = 0.5f))
        startX += stripeSpacing
    }
}

/**
 * Distant city silhouette and hills for visual depth.
 */
private fun DrawScope.drawDistantCityscape(cWidth: Float, groundY: Float) {
    val hillPath = Path().apply {
        moveTo(0f, groundY)
        cubicTo(cWidth * 0.25f, groundY - 60f, cWidth * 0.45f, groundY - 20f, cWidth * 0.7f, groundY - 80f)
        cubicTo(cWidth * 0.85f, groundY - 50f, cWidth * 0.95f, groundY - 30f, cWidth, groundY)
        close()
    }
    drawPath(hillPath, color = Color(0xFF2C394F).copy(alpha = 0.65f))

    // A few skyscraper silhouettes
    val buildings = listOf(
        Pair(40f, 90f),
        Pair(150f, 130f),
        Pair(270f, 80f),
        Pair(380f, 150f),
        Pair(540f, 110f),
        Pair(700f, 140f),
        Pair(880f, 95f)
    )
    buildings.forEach { (bx, bHeight) ->
        drawRect(
            color = Color(0xFF1E2638).copy(alpha = 0.55f),
            topLeft = Offset(bx, groundY - bHeight),
            size = Size(70f, bHeight)
        )
    }
}

/**
 * Renders cute layered cumulus clouds drifting across the sky.
 */
private fun DrawScope.drawSoftCloud(x: Float, y: Float, width: Float) {
    val cloudColor = Color.White.copy(alpha = 0.4f)
    val r = width * 0.22f
    drawCircle(cloudColor, radius = r, center = Offset(x + r, y))
    drawCircle(cloudColor, radius = r * 1.35f, center = Offset(x + width * 0.45f, y - r * 0.3f))
    drawCircle(cloudColor, radius = r * 0.95f, center = Offset(x + width * 0.75f, y))
    drawRoundRect(
        cloudColor,
        topLeft = Offset(x, y - r * 0.2f),
        size = Size(width, r * 1.2f),
        cornerRadius = CornerRadius(r, r)
    )
}

/**
 * Score explosion particle in heart shape.
 */
private fun DrawScope.drawHeartParticle(x: Float, y: Float, size: Float, alpha: Float) {
    if (size <= 0f) return
    val heartColor = RomanticPink.copy(alpha = alpha.coerceIn(0f, 1f))
    drawCircle(heartColor, radius = size * 0.4f, center = Offset(x - size * 0.2f, y - size * 0.2f))
    drawCircle(heartColor, radius = size * 0.4f, center = Offset(x + size * 0.2f, y - size * 0.2f))
    val tipPath = Path().apply {
        moveTo(x - size * 0.45f, y - size * 0.05f)
        lineTo(x, y + size * 0.55f)
        lineTo(x + size * 0.45f, y - size * 0.05f)
        close()
    }
    drawPath(tipPath, color = heartColor)
}

@Preview(name = "Phone - Flappy Bird", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Flappy Bird", showBackground = true, device = Devices.TABLET)
@Composable
fun FlappyLovePreview() {
    MyApplicationTheme {
        FlappyLoveScreen(
            appPreferences = AppPreferences(LocalContext.current)
        )
    }
}
