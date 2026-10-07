package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.AppConfig
import com.example.data.AppPreferences
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.theme.BlushPink
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.KeyGold
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SoftCream
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SubtleGrayText
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun CatchMyFacesScreen(
    appPreferences: AppPreferences,
    modifier: Modifier = Modifier
) {
    var score by remember { mutableIntStateOf(0) }
    var highScore by remember { mutableIntStateOf(appPreferences.getCatchFacesHighScore()) }
    var timeLeft by remember { mutableIntStateOf(AppConfig.GamesConfig.CATCH_FACES_ROUND_SECONDS) }
    var gameState by remember { mutableStateOf("START") } // "START", "PLAYING", "GAMEOVER"
    var activeSlot by remember { mutableIntStateOf(-1) }
    var currentFace by remember { mutableStateOf(AppConfig.GamesConfig.BOYFRIEND_FACES.first()) }
    var hitFeedbackMessage by remember { mutableStateOf<String?>(null) }

    fun startGame() {
        score = 0
        timeLeft = AppConfig.GamesConfig.CATCH_FACES_ROUND_SECONDS
        gameState = "PLAYING"
        activeSlot = -1
        hitFeedbackMessage = null
    }

    // Timer countdown
    LaunchedEffect(gameState) {
        if (gameState == "PLAYING") {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1
            }
            gameState = "GAMEOVER"
            if (score > highScore) {
                highScore = score
                appPreferences.saveCatchFacesScore(score)
            }
        }
    }

    // Slot appearance loop
    LaunchedEffect(gameState) {
        if (gameState == "PLAYING") {
            while (gameState == "PLAYING") {
                val nextSlot = Random.nextInt(9)
                val nextFace = AppConfig.GamesConfig.BOYFRIEND_FACES.random()
                currentFace = nextFace
                activeSlot = nextSlot
                delay(AppConfig.GamesConfig.CATCH_FACES_PEEK_TIME_MS)
                activeSlot = -1
                delay(150L)
            }
        }
    }

    fun onSlotTapped(slotIndex: Int) {
        if (gameState != "PLAYING") return
        if (slotIndex == activeSlot) {
            score += 1
            hitFeedbackMessage = currentFace.quote
            activeSlot = -1
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        FloatingHeartsCanvas(particleCount = 10, primaryColor = RomanticPink)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .align(Alignment.TopCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HUD
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Score: $score",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "High Score: $highScore",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfacePinkLight)
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "⏱ $timeLeft s",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (timeLeft <= 5) Color(0xFFE53935) else RomanticPink
                    )
                }
            }

            // Hit feedback quote
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                hitFeedbackMessage?.let { msg ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SurfacePinkLight)
                            .border(1.dp, RomanticPink.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "✨ $msg",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RomanticPink,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3x3 GRID
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (row in 0 until 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0 until 3) {
                            val slotIndex = row * 3 + col
                            val isActive = activeSlot == slotIndex

                            val scale by animateFloatAsState(
                                targetValue = if (isActive) 1.1f else 1f,
                                animationSpec = tween(150),
                                label = "slotScale"
                            )

                            Box(
                                modifier = Modifier
                                    .size(92.dp)
                                    .scale(scale)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (isActive) BlushPink
                                        else SurfacePinkLight
                                    )
                                    .border(
                                        if (isActive) 2.dp else 1.dp,
                                        if (isActive) RomanticPink else SurfaceCardBorder,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { onSlotTapped(slotIndex) }
                                    .testTag("catch_slot_$slotIndex"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isActive) {
                                    if (currentFace.imageResId != null) {
                                        Image(
                                            painter = painterResource(id = currentFace.imageResId!!),
                                            contentDescription = currentFace.name,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(6.dp)
                                                .clip(RoundedCornerShape(14.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = currentFace.emoji, fontSize = 40.sp)
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "•",
                                        fontSize = 24.sp,
                                        color = SoftPink.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // START OVERLAY
        AnimatedVisibility(
            visible = gameState == "START",
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                modifier = Modifier.padding(24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CrispWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🎯", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Reflex Challenge",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Target icons appear randomly across the grid.\nTap before the peek window expires!\n\nRound duration: 30s\nPeek window: 650ms\nTest your speed and beat your high score!",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { startGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = RomanticPink, contentColor = CrispWhite),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("catch_start_btn")
                    ) {
                        Text("Begin Challenge", fontWeight = FontWeight.SemiBold, color = CrispWhite)
                    }
                }
            }
        }

        // GAME OVER OVERLAY
        AnimatedVisibility(
            visible = gameState == "GAMEOVER",
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                modifier = Modifier.padding(24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CrispWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🏁", fontSize = 38.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Session Complete",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Score: $score   •   Best: $highScore",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { startGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = RomanticPink, contentColor = CrispWhite),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("catch_restart_btn")
                    ) {
                        Text("Play Again", fontWeight = FontWeight.SemiBold, color = CrispWhite)
                    }
                }
            }
        }
    }
}

@Preview(name = "Phone - Catch Faces", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Catch Faces", showBackground = true, device = Devices.TABLET)
@Composable
fun CatchMyFacesPreview() {
    com.example.ui.theme.MyApplicationTheme {
        CatchMyFacesScreen(
            appPreferences = com.example.data.AppPreferences(androidx.compose.ui.platform.LocalContext.current)
        )
    }
}
